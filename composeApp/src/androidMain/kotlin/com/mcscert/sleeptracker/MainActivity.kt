package com.mcscert.sleeptracker

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.mcscert.sleeptracker.db.SleepTrackerDatabase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.coroutines.resume
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    private var recordAudioPermissionContinuation: CancellableContinuation<Boolean>? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        val services = createAppServices(
            databaseDriverFactory = DatabaseDriverFactory(this),
            secureStore = SecureKeyValueStore(this),
            sensorGateway = SensorGateway(this),
            audioGateway = AudioGateway(this),
            backgroundTaskGateway = BackgroundTaskGateway(this),
            authHttpClient = createAuthHttpClient(),
            apiBaseUrl = resolveApiBaseUrl(),
        )
        setContent { SleepTrackerApp(services) }
    }

    private fun createAuthHttpClient(): HttpClient =
        HttpClient(OkHttp) {
            // MIGRATION: React Native fetch used JSON request/response bodies.
            //            Ktor's ContentNegotiation gives the same typed JSON
            //            boundary for `/auth/login` and `/auth/register`.
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; encodeDefaults = true })
            }
        }

    private fun resolveApiBaseUrl(): String =
        if (TransparencyDemoConfig.inDemoMode && !TransparencyDemoConfig.encryptedInTransit) {
            BuildConfig.API_UNENCRYPTED_URL
        } else {
            BuildConfig.API_ENCRYPTED_URL
        }

    suspend fun requestRecordAudioPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return suspendCancellableCoroutine { continuation ->
            // MIGRATION: Expo requested microphone permission from JS. Android
            //            requires the Activity to own the runtime permission
            //            callback, so the KMP actual delegates the request here.
            recordAudioPermissionContinuation = continuation
            continuation.invokeOnCancellation { recordAudioPermissionContinuation = null }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST) {
            val granted = grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
            recordAudioPermissionContinuation?.resume(granted)
            recordAudioPermissionContinuation = null
        }
    }

    private companion object {
        const val RECORD_AUDIO_PERMISSION_REQUEST = 2101
    }
}

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(SleepTrackerDatabase.Schema, context, "sleeptracker_data.db")
}

actual class SecureKeyValueStore(private val context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "sleep_tracker_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    actual suspend fun read(key: String): String? = prefs.getString(key, null)
    actual suspend fun write(key: String, value: String) {
        // MIGRATION: Expo SecureStore becomes a KMP secure storage boundary.
        //            Android stores the token and user payload in
        //            EncryptedSharedPreferences, which wraps keys with Android
        //            Keystore so credentials are not persisted as plaintext.
        prefs.edit().putString(key, value).apply()
    }
    actual suspend fun delete(key: String) {
        prefs.edit().remove(key).apply()
    }
}

actual class CryptoProvider actual constructor(private val store: SecureKeyValueStore) {
    private val salt = "sleeptracker-aes-v1-salt".toByteArray(Charsets.UTF_8)
    private val iterations = 10_000

    actual suspend fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return plainText
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16).also { SecureRandom().nextBytes(it) }
        cipher.init(Cipher.ENCRYPT_MODE, key(), IvParameterSpec(iv))
        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.NO_WRAP)
    }

    actual suspend fun decrypt(cipherText: String): String {
        if (cipherText.isEmpty()) return cipherText
        return runCatching {
            val bytes = Base64.decode(cipherText, Base64.NO_WRAP)
            val iv = bytes.copyOfRange(0, 16)
            val payload = bytes.copyOfRange(16, bytes.size)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, key(), IvParameterSpec(iv))
            String(cipher.doFinal(payload), Charsets.UTF_8)
        }.getOrDefault(cipherText)
    }

    private suspend fun key(): SecretKeySpec {
        val stored = store.read("myAppEncryptionKey") ?: randomPassphrase().also {
            store.write("myAppEncryptionKey", it)
        }
        val spec = PBEKeySpec(stored.toCharArray(), salt, iterations, 256)
        val secret = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        return SecretKeySpec(secret, "AES")
    }

    private fun randomPassphrase(): String {
        val bytes = ByteArray(32).also { SecureRandom().nextBytes(it) }
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}

actual class SensorGateway(private val context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var listener: SensorEventListener? = null

    actual suspend fun isLightAvailable(): Boolean =
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null
    actual suspend fun isAccelerometerAvailable(): Boolean =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
    actual suspend fun isAudioAvailable(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    actual suspend fun startAccelerometer(onSample: (AccelerometerSensorData) -> Unit) {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return
        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values.getOrNull(0) ?: 0f
                val y = event.values.getOrNull(1) ?: 0f
                val z = event.values.getOrNull(2) ?: 0f
                val magnitude = sqrt((x * x + y * y + z * z).toDouble())
                onSample(
                    AccelerometerSensorData(
                        id = newId(),
                        userId = "",
                        timestamp = Clock.System.now().toEpochMilliseconds().toString(),
                        date = todayIso(),
                        x = x.toString(),
                        y = y.toString(),
                        z = z.toString(),
                        magnitude = magnitude.toString(),
                        movementIntensity = when {
                            magnitude < 0.1 -> MovementIntensity.Still
                            magnitude < 0.5 -> MovementIntensity.Light
                            magnitude < 1.0 -> MovementIntensity.Moderate
                            else -> MovementIntensity.Active
                        },
                    )
                )
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    actual suspend fun stopAccelerometer() {
        listener?.let(sensorManager::unregisterListener)
        listener = null
    }
}

actual class AudioGateway(private val context: Context) {
    actual suspend fun requestPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        // MIGRATION_FLAG: Permission dialogs require an Activity context on
        //                 Android. MainActivity supplies that context in normal
        //                 app startup; tests/background contexts return false.
        return (context as? MainActivity)?.requestRecordAudioPermission() ?: false
    }
}

actual class BackgroundTaskGateway(private val context: Context) {
    actual suspend fun updateAccelerometerEnabled(enabled: Boolean) {
        val intent = Intent(context, AccelerometerForegroundService::class.java)
        if (enabled) {
            ContextCompat.startForegroundService(context, intent)
        } else {
            context.stopService(intent)
        }
    }
}

class AccelerometerForegroundService : Service() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Sleep Tracker Sensors", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // MIGRATION: Expo TaskManager/BackgroundFetch maps to an Android
        //            ForegroundService with a persistent notification so
        //            Android 8+ allows background accelerometer collection.
        startForeground(42, notification())
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun notification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentTitle("GPT Sleep Tracker KMP")
            .setContentText("Tracking movement for sleep transparency")
            .setOngoing(true)
            .build()

    private companion object {
        const val CHANNEL_ID = "sleep_tracker_sensor_service"
    }
}
