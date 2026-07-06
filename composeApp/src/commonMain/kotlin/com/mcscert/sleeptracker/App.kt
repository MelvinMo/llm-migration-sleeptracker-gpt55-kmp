@file:Suppress("FunctionName")

package com.mcscert.sleeptracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.cash.sqldelight.db.SqlDriver
import com.mcscert.sleeptracker.db.SleepTrackerDatabase
import com.mcscert.sleeptracker.resources.Res
import com.mcscert.sleeptracker.resources.accelerometer_cloud_high
import com.mcscert.sleeptracker.resources.accelerometer_cloud_low
import com.mcscert.sleeptracker.resources.accelerometer_cloud_medium
import com.mcscert.sleeptracker.resources.accelerometer_local_high
import com.mcscert.sleeptracker.resources.accelerometer_local_low
import com.mcscert.sleeptracker.resources.accelerometer_local_medium
import com.mcscert.sleeptracker.resources.bedroom_light_bg
import com.mcscert.sleeptracker.resources.journal_bg
import com.mcscert.sleeptracker.resources.light_cloud_high
import com.mcscert.sleeptracker.resources.light_cloud_low
import com.mcscert.sleeptracker.resources.light_cloud_medium
import com.mcscert.sleeptracker.resources.light_local_high
import com.mcscert.sleeptracker.resources.light_local_low
import com.mcscert.sleeptracker.resources.light_local_medium
import com.mcscert.sleeptracker.resources.microphone_bg
import com.mcscert.sleeptracker.resources.microphone_cloud_high
import com.mcscert.sleeptracker.resources.microphone_cloud_low
import com.mcscert.sleeptracker.resources.microphone_cloud_medium
import com.mcscert.sleeptracker.resources.microphone_local_high
import com.mcscert.sleeptracker.resources.microphone_local_low
import com.mcscert.sleeptracker.resources.microphone_local_medium
import com.mcscert.sleeptracker.resources.privacy_high
import com.mcscert.sleeptracker.resources.privacy_high_open
import com.mcscert.sleeptracker.resources.privacy_low
import com.mcscert.sleeptracker.resources.privacy_low_open
import com.mcscert.sleeptracker.resources.privacy_medium
import com.mcscert.sleeptracker.resources.privacy_medium_open
import com.mcscert.sleeptracker.resources.running_bg
import com.mcscert.sleeptracker.resources.sleep_duration_graph
import com.mcscert.sleeptracker.resources.sleep_duration_wheel
import com.mcscert.sleeptracker.resources.sleep_mode_bg
import com.mcscert.sleeptracker.resources.sleep_quality_daily
import com.mcscert.sleeptracker.resources.sleep_quality_graph
import com.mcscert.sleeptracker.resources.sleep_stages_daily
import com.mcscert.sleeptracker.resources.space_mono_regular
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

object AppColors {
    val AppBackground = Color(0xFF1A1A2E)
    val GeneralBlue = Color(0xFF39ACE7)
    val Accent = Color(0xFF4A90D9)
    val LightBlack = Color(0xFF181719)
    val InputFieldBackground = Color(0xFF5B5775)
    val InputFieldPlaceholder = Color(0xFFAFA3BF)
    val InputFieldSelected = Color(0xFFF2D8A7)
    val HyperlinkBlue = Color(0xFF4A90E2)
    val TooltipGreen = Color(0xFFE0FFDF)
    val TooltipYellow = Color(0xFFFFFD86)
    val TooltipRed = Color(0xFFFD8686)
    val LightGrey = Color(0xFF888888)
    val Grey = Color(0x80EBEBF5)
}

object TransparencyUiConfig {
    const val journalTooltipEnabled: Boolean = true
    const val sleepPageTooltipEnabled: Boolean = true
    const val sleepModeTooltipEnabled: Boolean = true
}

object TransparencyDemoConfig {
    const val inDemoMode: Boolean = true
    const val collectAudio: Boolean = false
    const val collectLight: Boolean = false
    const val collectAccelerometer: Boolean = false
    const val encryptedAtRest: Boolean = false
    const val encryptedInTransit: Boolean = false
}

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Ready<T>(val value: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

@Serializable
data class User(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String = "",
)

@Serializable
data class AuthLoginRequest(val email: String, val password: String)

@Serializable
data class AuthRegisterRequest(val firstName: String, val lastName: String, val email: String, val password: String)

@Serializable
data class AuthResponse(val message: String = "", val user: User, val token: String)

@Serializable
data class ApiErrorResponse(val message: String = "API request failed")

class AuthApiClient(private val httpClient: HttpClient, private val baseUrl: String, private val json: Json) {
    suspend fun login(email: String, password: String): AuthResponse =
        postAuth(path = "/auth/login", body = AuthLoginRequest(email = email, password = password))

    suspend fun register(firstName: String, lastName: String, email: String, password: String): AuthResponse =
        postAuth(path = "/auth/register", body = AuthRegisterRequest(firstName = firstName, lastName = lastName, email = email, password = password))

    private suspend inline fun <reified T> postAuth(path: String, body: T): AuthResponse {
        // MIGRATION: React Native used CloudStorageService.post('/auth/...').
        //            KMP uses Ktor with the same JSON body and the same
        //            success/error contract, so invalid credentials stay invalid.
        val response = httpClient.post("${baseUrl.trimEnd('/')}$path") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        val rawBody = response.bodyAsText()
        if (response.status.isSuccess()) {
            return json.decodeFromString<AuthResponse>(rawBody)
        }

        // MIGRATION_FLAG: Some dev backends return HTML fallback errors. Decode
        //                 JSON error bodies when possible, otherwise expose a
        //                 safe network/API error instead of rendering raw HTML.
        val apiMessage = runCatching { json.decodeFromString<ApiErrorResponse>(rawBody).message }.getOrNull()
        throw IllegalStateException(apiMessage ?: "API request failed (${response.status.value})")
    }
}

@Serializable
data class UserConsentPreferences(
    val accelerometerEnabled: Boolean = false,
    val lightSensorEnabled: Boolean = false,
    val microphoneEnabled: Boolean = false,
    val cloudStorageEnabled: Boolean = false,
    val agreedToPrivacyPolicy: Boolean = false,
    val analyticsEnabled: Boolean = false,
    val marketingCommunications: Boolean = false,
    val notificationsEnabled: Boolean = false,
)

enum class SleepNote(val label: String) {
    Pain("Pain"),
    Stress("Stress"),
    Anxiety("Anxiety"),
    Medication("Medication"),
    Caffeine("Caffeine"),
    Alcohol("Alcohol"),
    WarmBath("Warm Bath"),
    HeavyMeal("Heavy Meal");
}

@Serializable
data class JournalData(
    val date: String,
    val userId: String,
    val journalId: String,
    val bedtime: String = "",
    val alarmTime: String = "",
    val sleepDuration: String = "",
    val diaryEntry: String = "",
    val sleepNotes: List<SleepNote> = emptyList(),
)

@Serializable
data class JournalPatch(
    val date: String? = null,
    val bedtime: String? = null,
    val alarmTime: String? = null,
    val sleepDuration: String? = null,
    val diaryEntry: String? = null,
    val sleepNotes: List<SleepNote>? = null,
)

@Serializable
data class GeneralSleepData(
    val userId: String,
    val currentSleepDuration: String,
    val snoring: String,
    val tirednessFrequency: String,
    val daytimeSleepiness: String,
)

enum class AmbientNoiseLevel(val wireName: String) { Quiet("quiet"), Moderate("moderate"), Loud("loud"), VeryLoud("very_loud") }
enum class LightLevel(val wireName: String) { Dark("dark"), Dim("dim"), Moderate("moderate"), Bright("bright") }
enum class MovementIntensity(val wireName: String) { Still("still"), Light("light"), Moderate("moderate"), Active("active") }

sealed interface SensorData {
    val id: String
    val userId: String
    val timestamp: String
    val date: String
    val sensorType: String
}

@Serializable
data class FrequencyBands(val low: String, val mid: String, val high: String)

@Serializable
data class AudioSensorData(
    override val id: String,
    override val userId: String,
    override val timestamp: String,
    override val date: String,
    val averageDecibels: String,
    val peakDecibels: String,
    val frequencyBands: FrequencyBands,
    val audioClipUri: String? = null,
    val snoreDetected: Boolean,
    val ambientNoiseLevel: AmbientNoiseLevel,
) : SensorData {
    override val sensorType: String = "audio"
}

@Serializable
data class LightSensorData(
    override val id: String,
    override val userId: String,
    override val timestamp: String,
    override val date: String,
    val illuminance: String,
    val lightLevel: LightLevel,
) : SensorData {
    override val sensorType: String = "light"
}

@Serializable
data class AccelerometerSensorData(
    override val id: String,
    override val userId: String,
    override val timestamp: String,
    override val date: String,
    val x: String,
    val y: String,
    val z: String,
    val magnitude: String,
    val movementIntensity: MovementIntensity,
) : SensorData {
    override val sensorType: String = "accelerometer"
}

enum class DataType(val wireName: String) {
    SensorAudio("SENSOR_AUDIO"),
    SensorMotion("SENSOR_MOTION"),
    SensorLight("SENSOR_LIGHT"),
    UserJournal("USER_JOURNAL"),
    UserProfile("USER_PROFILE"),
    GeneralSleep("GENERAL_SLEEP"),
    SleepStatistics("SLEEP_STATISTICS"),
    DeviceInfo("DEVICE_INFO"),
    Location("LOCATION"),
    UsageAnalytics("USAGE_ANALYTICS"),
}

enum class DataSource(val wireName: String) {
    Microphone("MICROPHONE"),
    Accelerometer("ACCELEROMETER"),
    LightSensor("LIGHT_SENSOR"),
    UserInput("USER_INPUT"),
    DerivedData("DERIVED_DATA"),
    SystemInfo("SYSTEM_INFO"),
}

enum class DataDestination(val wireName: String) {
    AsyncStorage("ASYNC_STORAGE"),
    SecureStore("SECURE_STORE"),
    SqliteDb("SQLITE_DB"),
    Memory("MEMORY"),
    GoogleCloud("GOOGLE_CLOUD"),
    ThirdParty("THIRD_PARTY"),
}

enum class EncryptionMethod(val wireName: String) { None("NONE"), Aes256("AES_256"), Jwt("JWT"), DeviceKeychain("DEVICE_KEYCHAIN") }
enum class PrivacyRisk { Low, Medium, High }
enum class RegulatoryFramework { Pipeda, Phipa, Gdpr }

@Serializable
data class RegulatoryCompliance(
    val framework: RegulatoryFramework = RegulatoryFramework.Pipeda,
    val compliant: Boolean = true,
    val issues: String = "",
    val relevantSections: List<String> = emptyList(),
)

@Serializable
data class AiExplanation(
    val why: String,
    val storage: String = "",
    val access: String = "",
    val privacyExplanation: String = "",
    val privacyPolicyLink: List<String> = emptyList(),
    val regulationLink: List<String> = emptyList(),
)

@Serializable
data class TransparencyEvent(
    val timestamp: String? = null,
    val dataType: DataType,
    val source: DataSource,
    val sensorType: String? = null,
    val samplingRate: Int? = null,
    val duration: Int? = null,
    val encryptionMethod: EncryptionMethod? = null,
    val storageLocation: DataDestination? = null,
    val endpoint: String? = null,
    val protocol: String? = null,
    val backgroundMode: Boolean? = null,
    val privacyRisk: PrivacyRisk = PrivacyRisk.Low,
    val regulatoryCompliance: RegulatoryCompliance = RegulatoryCompliance(),
    val aiExplanation: AiExplanation,
)

enum class TransparencyChannel(val storageKey: String) {
    Light("lightSensorTransparency"),
    Microphone("microphoneTransparency"),
    Accelerometer("accelerometerTransparency"),
    Journal("journalTransparency"),
    Sleep("generalSleepTransparency"),
    Statistics("statisticsTransparency"),
}

data class TransparencyState(
    val light: TransparencyEvent = defaultLightTransparency(),
    val microphone: TransparencyEvent = defaultMicrophoneTransparency(),
    val accelerometer: TransparencyEvent = defaultAccelerometerTransparency(),
    val journal: TransparencyEvent = defaultJournalTransparency(),
    val sleep: TransparencyEvent = defaultSleepTransparency(),
    val statistics: TransparencyEvent = defaultStatisticsTransparency(),
)

fun defaultJournalTransparency() = TransparencyEvent(
    dataType = DataType.UserJournal,
    source = DataSource.UserInput,
    aiExplanation = AiExplanation("To analyze how your daily mood, habits, sleep goals affects your sleep quality."),
)
fun defaultLightTransparency() = TransparencyEvent(
    dataType = DataType.SensorLight,
    source = DataSource.LightSensor,
    aiExplanation = AiExplanation("To understand how the light conditions in your sleep environment may affect your sleep quality"),
)
fun defaultMicrophoneTransparency() = TransparencyEvent(
    dataType = DataType.SensorAudio,
    source = DataSource.Microphone,
    aiExplanation = AiExplanation("To analyze sleep disturbances such as snoring and talking, as well as understanding the noise level in your sleep environment"),
)
fun defaultAccelerometerTransparency() = TransparencyEvent(
    dataType = DataType.SensorMotion,
    source = DataSource.Accelerometer,
    aiExplanation = AiExplanation("To analyze how your movements during sleep and throughout the day impact sleep quality"),
)
fun defaultSleepTransparency() = TransparencyEvent(
    dataType = DataType.GeneralSleep,
    source = DataSource.UserInput,
    aiExplanation = AiExplanation("To understand your current sleep quality and how we can improve it"),
)
fun defaultStatisticsTransparency() = TransparencyEvent(
    dataType = DataType.SleepStatistics,
    source = DataSource.DerivedData,
    aiExplanation = AiExplanation(
        why = "Provide summaries and actionable insights to help improve your sleep quality",
        privacyExplanation = "No privacy risks",
        storage = "This data is stored securely in your preferred storage location with encryption.",
        access = "No third parties have access to this data. Only you can view it through the app.",
        privacyPolicyLink = listOf("derivedData"),
        regulationLink = listOf("access"),
    ),
)

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
expect class SecureKeyValueStore {
    suspend fun read(key: String): String?
    suspend fun write(key: String, value: String)
    suspend fun delete(key: String)
}
expect class CryptoProvider(store: SecureKeyValueStore) {
    suspend fun encrypt(plainText: String): String
    suspend fun decrypt(cipherText: String): String
}
expect class SensorGateway {
    suspend fun isLightAvailable(): Boolean
    suspend fun isAccelerometerAvailable(): Boolean
    suspend fun isAudioAvailable(): Boolean
    suspend fun startAccelerometer(onSample: (AccelerometerSensorData) -> Unit)
    suspend fun stopAccelerometer()
}
expect class AudioGateway {
    suspend fun requestPermission(): Boolean
}
expect class BackgroundTaskGateway {
    suspend fun updateAccelerometerEnabled(enabled: Boolean)
}

class AppServices(
    val authRepository: AuthRepository,
    val profileRepository: ProfileRepository,
    val journalRepository: JournalRepository,
    val generalSleepRepository: GeneralSleepRepository,
    val transparencyStore: TransparencyStore,
    val sensorGateway: SensorGateway,
    val audioGateway: AudioGateway,
    val backgroundTaskGateway: BackgroundTaskGateway,
)

fun createAppServices(
    databaseDriverFactory: DatabaseDriverFactory,
    secureStore: SecureKeyValueStore,
    sensorGateway: SensorGateway,
    audioGateway: AudioGateway,
    backgroundTaskGateway: BackgroundTaskGateway,
    authHttpClient: HttpClient,
    apiBaseUrl: String,
): AppServices {
    val database = SleepTrackerDatabase(databaseDriverFactory.createDriver())
    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    val crypto = CryptoProvider(secureStore)
    val transparencyStore = TransparencyStore(secureStore, json)
    val authRepository = AuthRepository(secureStore, json, AuthApiClient(authHttpClient, apiBaseUrl, json))
    val profileRepository = ProfileRepository(secureStore, json)
    val localJournal = LocalJournalDataSource(database, json)
    val cloudJournal = CloudJournalDataSource()
    val journalRepository = JournalRepository(localJournal, cloudJournal, crypto, authRepository, profileRepository, transparencyStore)
    val generalSleepRepository = GeneralSleepRepository(secureStore, crypto, authRepository, json)
    return AppServices(authRepository, profileRepository, journalRepository, generalSleepRepository, transparencyStore, sensorGateway, audioGateway, backgroundTaskGateway)
}

class AuthRepository(private val store: SecureKeyValueStore, private val json: Json, private val authApiClient: AuthApiClient) {
    suspend fun currentUser(): User? {
        val token = store.read("authToken") ?: return null
        if (token.isBlank() || token == "demo-token") {
            // MIGRATION: Earlier scaffold builds stored a fake token. The RN app
            //            only treats backend JWTs as authenticated, so KMP clears
            //            fake or empty tokens to prevent silently logging in.
            logout()
            return null
        }
        return store.read("authUser")?.let { json.decodeFromString<User>(it) }
    }
    private fun requireAuthToken(auth: AuthResponse): String {
        if (auth.token.isBlank()) {
            // MIGRATION: The original React Native flow only persisted a
            //            session after `/auth/login` or `/auth/register`
            //            returned a backend JWT. KMP rejects malformed 2xx
            //            responses so credentials are never evaluated locally.
            throw IllegalStateException("Authentication response missing token.")
        }
        return auth.token
    }
    suspend fun login(email: String, password: String): User {
        // MIGRATION: React Native called `/auth/login`, then persisted only the
        //            returned user and JWT. KMP follows the same order so wrong
        //            credentials never create local auth state.
        logout()
        val auth = authApiClient.login(email, password)
        val token = requireAuthToken(auth)
        val user = auth.user
        store.write("authUser", json.encodeToString(user))
        store.write("authToken", token)
        return user
    }
    suspend fun register(firstName: String, lastName: String, email: String, password: String): User {
        // MIGRATION: Registration remains a repository operation because RN's
        //            Zustand store did not let screens talk to fetch directly.
        logout()
        val auth = authApiClient.register(firstName, lastName, email, password)
        val token = requireAuthToken(auth)
        val user = auth.user
        store.write("authUser", json.encodeToString(user))
        store.write("authToken", token)
        return user
    }
    suspend fun logout() {
        store.delete("authUser")
        store.delete("authToken")
    }
}

class ProfileRepository(private val store: SecureKeyValueStore, private val json: Json) {
    suspend fun preferences(): UserConsentPreferences =
        store.read("userConsentPreferences")?.let { json.decodeFromString<UserConsentPreferences>(it) } ?: UserConsentPreferences()
    suspend fun savePreferences(preferences: UserConsentPreferences) {
        store.write("userConsentPreferences", json.encodeToString(preferences))
    }
    suspend fun privacyComplete(): Boolean = store.read("hasCompletedPrivacyOnboarding") == "true"
    suspend fun appComplete(): Boolean = store.read("hasCompletedAppOnboarding") == "true"
    suspend fun setPrivacyComplete(value: Boolean) = store.write("hasCompletedPrivacyOnboarding", value.toString())
    suspend fun setAppComplete(value: Boolean) = store.write("hasCompletedAppOnboarding", value.toString())
}

class TransparencyStore(private val store: SecureKeyValueStore, private val json: Json) {
    private val _state = MutableStateFlow(TransparencyState())
    val state: StateFlow<TransparencyState> = _state.asStateFlow()

    suspend fun load() {
        var loaded = TransparencyState()
        for (channel in TransparencyChannel.entries) {
            val event = store.read(channel.storageKey)?.let { json.decodeFromString<TransparencyEvent>(it) }
            if (event != null) loaded = loaded.withChannel(channel, event)
        }
        _state.value = loaded
    }

    suspend fun setChannel(channel: TransparencyChannel, event: TransparencyEvent) {
        // MIGRATION: Zustand persisted each transparency setter independently.
        //            StateFlow.update gives the KMP store the same six atomic
        //            reactive channels without an untyped global object.
        store.write(channel.storageKey, json.encodeToString(event))
        _state.update { it.withChannel(channel, event) }
    }
}

fun TransparencyState.withChannel(channel: TransparencyChannel, event: TransparencyEvent): TransparencyState =
    when (channel) {
        TransparencyChannel.Light -> copy(light = event)
        TransparencyChannel.Microphone -> copy(microphone = event)
        TransparencyChannel.Accelerometer -> copy(accelerometer = event)
        TransparencyChannel.Journal -> copy(journal = event)
        TransparencyChannel.Sleep -> copy(sleep = event)
        TransparencyChannel.Statistics -> copy(statistics = event)
    }

interface JournalDataSource {
    suspend fun getJournalByDate(userId: String, date: String): JournalData?
    suspend fun editJournal(date: String, patch: JournalPatch, userId: String): JournalData?
}

class LocalJournalDataSource(private val database: SleepTrackerDatabase, private val json: Json) : JournalDataSource {
    override suspend fun getJournalByDate(userId: String, date: String): JournalData? =
        database.sleepTrackerDatabaseQueries.selectJournalByDate(userId, date).executeAsOneOrNull()?.let {
            JournalData(
                date = it.date,
                userId = it.userId,
                journalId = it.journalId,
                bedtime = it.bedtime.orEmpty(),
                alarmTime = it.alarmTime.orEmpty(),
                sleepDuration = it.sleepDuration.orEmpty(),
                diaryEntry = it.diaryEntry.orEmpty(),
                sleepNotes = decodeNotes(it.sleepNotes),
            )
        }

    override suspend fun editJournal(date: String, patch: JournalPatch, userId: String): JournalData? {
        val existing = getJournalByDate(userId, date)
        if (existing == null) {
            database.sleepTrackerDatabaseQueries.insertJournal(
                journalId = newId(),
                userId = userId,
                date = patch.date ?: date,
                bedtime = patch.bedtime.orEmpty(),
                alarmTime = patch.alarmTime.orEmpty(),
                sleepDuration = patch.sleepDuration.orEmpty(),
                diaryEntry = patch.diaryEntry.orEmpty(),
                sleepNotes = encodeNotes(patch.sleepNotes ?: emptyList()),
                createdAt = nowIso(),
            )
        } else {
            database.sleepTrackerDatabaseQueries.updateJournal(
                bedtime = patch.bedtime,
                alarmTime = patch.alarmTime,
                sleepDuration = patch.sleepDuration,
                diaryEntry = patch.diaryEntry,
                sleepNotes = patch.sleepNotes?.let(::encodeNotes),
                userId = userId,
                date = date,
            )
        }
        return getJournalByDate(userId, date)
    }

    private fun encodeNotes(notes: List<SleepNote>): String = json.encodeToString(notes.map { it.label })
    private fun decodeNotes(raw: String?): List<SleepNote> =
        raw?.let { runCatching { json.decodeFromString<List<String>>(it) }.getOrDefault(emptyList()) }
            ?.mapNotNull { label -> SleepNote.entries.firstOrNull { it.label == label } }
            ?: emptyList()
}

class CloudJournalDataSource : JournalDataSource {
    override suspend fun getJournalByDate(userId: String, date: String): JournalData? {
        // MIGRATION_FLAG: Cloud implementation is intentionally isolated so a
        //                 non-JSON HTML fallback from the backend cannot leak
        //                 into Compose UI. Complete Ktor endpoint mapping here.
        throw IllegalStateException("Cloud journal endpoint unavailable in local KMP scaffold")
    }
    override suspend fun editJournal(date: String, patch: JournalPatch, userId: String): JournalData? {
        throw IllegalStateException("Cloud journal endpoint unavailable in local KMP scaffold")
    }
}

class JournalRepository(
    private val local: JournalDataSource,
    private val cloud: JournalDataSource,
    private val crypto: CryptoProvider,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val transparencyStore: TransparencyStore,
) {
    suspend fun getJournalByDate(date: String): JournalData? {
        val user = authRepository.currentUser() ?: return null
        val preferences = profileRepository.preferences()
        val source = if (preferences.cloudStorageEnabled) cloud else local
        val encrypted = runCatching { source.getJournalByDate(user.userId, date) }
            .recoverCatching {
                // MIGRATION: Prior Flutter feedback exposed raw backend
                //            `<!DOCTYPE html>` on Journal. KMP preserves the
                //            Repository pattern and falls back to SQLDelight.
                transparencyStore.setChannel(TransparencyChannel.Journal, defaultJournalTransparency().copy(storageLocation = DataDestination.SqliteDb))
                local.getJournalByDate(user.userId, date)
            }.getOrNull()
        return encrypted?.decryptJournal(crypto)
    }

    suspend fun editJournal(date: String, patch: JournalPatch): JournalData? {
        val user = authRepository.currentUser() ?: return null
        transparencyStore.setChannel(TransparencyChannel.Journal, defaultJournalTransparency())
        val encryptedPatch = patch.encryptJournalPatch(crypto)
        val preferences = profileRepository.preferences()
        val source = if (preferences.cloudStorageEnabled) cloud else local
        val encrypted = runCatching { source.editJournal(date, encryptedPatch, user.userId) }
            .recoverCatching {
                transparencyStore.setChannel(TransparencyChannel.Journal, defaultJournalTransparency().copy(storageLocation = DataDestination.SqliteDb))
                local.editJournal(date, encryptedPatch, user.userId)
            }.getOrNull()
        return encrypted?.decryptJournal(crypto)
    }
}

class GeneralSleepRepository(
    private val store: SecureKeyValueStore,
    private val crypto: CryptoProvider,
    private val authRepository: AuthRepository,
    private val json: Json,
) {
    suspend fun createSleepData(data: GeneralSleepData) {
        val user = authRepository.currentUser() ?: return
        val encrypted = data.copy(currentSleepDuration = crypto.encrypt(data.currentSleepDuration), userId = user.userId)
        store.write("sleepData_${user.userId}", json.encodeToString(encrypted))
    }
}

suspend fun JournalPatch.encryptJournalPatch(crypto: CryptoProvider): JournalPatch =
    if (TransparencyDemoConfig.inDemoMode && !TransparencyDemoConfig.encryptedAtRest) this else copy(
        bedtime = bedtime?.let { crypto.encrypt(it) },
        alarmTime = alarmTime?.let { crypto.encrypt(it) },
        sleepDuration = sleepDuration?.let { crypto.encrypt(it) },
        diaryEntry = diaryEntry?.let { crypto.encrypt(it) },
    )

suspend fun JournalData.decryptJournal(crypto: CryptoProvider): JournalData =
    if (TransparencyDemoConfig.inDemoMode && !TransparencyDemoConfig.encryptedAtRest) this else copy(
        bedtime = crypto.decrypt(bedtime),
        alarmTime = crypto.decrypt(alarmTime),
        sleepDuration = crypto.decrypt(sleepDuration),
        diaryEntry = crypto.decrypt(diaryEntry),
    )

data class AuthState(val user: User? = null, val loading: Boolean = false, val error: String? = null) {
    val isAuthenticated: Boolean = user != null
}

class AuthViewModel(private val services: AppServices) : ViewModel() {
    private val _state = MutableStateFlow(AuthState(loading = true))
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = AuthState(user = services.authRepository.currentUser())
        }
    }

    fun login(email: String, password: String, onDone: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            runCatching { services.authRepository.login(email, password) }
                .onSuccess { _state.value = AuthState(user = it); onDone() }
                .onFailure { _state.value = AuthState(error = it.message) }
        }
    }

    fun register(firstName: String, lastName: String, email: String, password: String, onDone: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            runCatching { services.authRepository.register(firstName, lastName, email, password) }
                .onSuccess { _state.value = AuthState(user = it); onDone() }
                .onFailure { _state.value = AuthState(error = it.message) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            services.authRepository.logout()
            _state.value = AuthState()
        }
    }
}

data class ProfileState(
    val preferences: UserConsentPreferences = UserConsentPreferences(),
    val privacyOnboardingComplete: Boolean = false,
    val appOnboardingComplete: Boolean = false,
)

class ProfileViewModel(private val services: AppServices) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch { load() }
    }

    suspend fun load() {
        _state.value = ProfileState(
            preferences = services.profileRepository.preferences(),
            privacyOnboardingComplete = services.profileRepository.privacyComplete(),
            appOnboardingComplete = services.profileRepository.appComplete(),
        )
    }

    fun setPreferences(preferences: UserConsentPreferences) {
        viewModelScope.launch {
            services.profileRepository.savePreferences(preferences)
            _state.update { it.copy(preferences = preferences) }
        }
    }

    fun setAccelerometerEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val updated = _state.value.preferences.copy(accelerometerEnabled = enabled)
            services.profileRepository.savePreferences(updated)
            services.backgroundTaskGateway.updateAccelerometerEnabled(enabled)
            _state.update { it.copy(preferences = updated) }
        }
    }

    fun completePrivacy(onDone: () -> Unit) {
        viewModelScope.launch {
            services.profileRepository.setPrivacyComplete(true)
            _state.update { it.copy(privacyOnboardingComplete = true) }
            onDone()
        }
    }

    fun completeApp(onDone: () -> Unit) {
        viewModelScope.launch {
            services.profileRepository.setAppComplete(true)
            _state.update { it.copy(appOnboardingComplete = true) }
            onDone()
        }
    }
}

class TransparencyViewModel(private val services: AppServices) : ViewModel() {
    val state: StateFlow<TransparencyState> = services.transparencyStore.state
    init { viewModelScope.launch { services.transparencyStore.load() } }
}

data class SleepState(val bedtime: String = "", val alarm: String = "", val loading: Boolean = true)

class SleepViewModel(private val services: AppServices) : ViewModel() {
    private val _state = MutableStateFlow(SleepState())
    val state: StateFlow<SleepState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            val journal = services.journalRepository.getJournalByDate(todayIso())
            _state.value = SleepState(bedtime = journal?.bedtime.orEmpty(), alarm = journal?.alarmTime.orEmpty(), loading = false)
        }
    }

    fun saveBedtime(value: String) {
        viewModelScope.launch {
            val alarm = _state.value.alarm
            val result = services.journalRepository.editJournal(todayIso(), JournalPatch(date = todayIso(), bedtime = value, sleepDuration = if (value.isNotBlank() && alarm.isNotBlank()) "8 hours" else ""))
            _state.update { it.copy(bedtime = result?.bedtime ?: value) }
        }
    }

    fun saveAlarm(value: String) {
        viewModelScope.launch {
            val bedtime = _state.value.bedtime
            val result = services.journalRepository.editJournal(todayIso(), JournalPatch(date = todayIso(), alarmTime = value, sleepDuration = if (bedtime.isNotBlank() && value.isNotBlank()) "8 hours" else ""))
            _state.update { it.copy(alarm = result?.alarmTime ?: value) }
        }
    }
}

data class JournalState(
    val selectedDate: String = todayIso(),
    val diaryEntry: String = "",
    val sleepNotes: List<SleepNote> = emptyList(),
    val bedtime: String = "",
    val alarm: String = "",
    val sleepGoal: String = "",
    val loading: Boolean = true,
    val error: String? = null,
)

class JournalViewModel(private val services: AppServices) : ViewModel() {
    private val _state = MutableStateFlow(JournalState())
    val state: StateFlow<JournalState> = _state.asStateFlow()

    init { load(todayIso()) }

    fun load(date: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, selectedDate = date, error = null) }
            runCatching { services.journalRepository.getJournalByDate(date) }
                .onSuccess { journal ->
                    _state.value = JournalState(
                        selectedDate = date,
                        diaryEntry = journal?.diaryEntry.orEmpty(),
                        sleepNotes = journal?.sleepNotes ?: emptyList(),
                        bedtime = journal?.bedtime.orEmpty(),
                        alarm = journal?.alarmTime.orEmpty(),
                        sleepGoal = journal?.sleepDuration.orEmpty(),
                        loading = false,
                    )
                }
                .onFailure { _state.update { old -> old.copy(loading = false, error = "Failed to load journal data") } }
        }
    }

    fun saveDiary(value: String) {
        viewModelScope.launch {
            val state = _state.value
            val result = services.journalRepository.editJournal(state.selectedDate, JournalPatch(date = state.selectedDate, diaryEntry = value))
            _state.update { it.copy(diaryEntry = result?.diaryEntry ?: value, error = null) }
        }
    }

    fun saveNotes(notes: List<SleepNote>) {
        viewModelScope.launch {
            val state = _state.value
            val result = services.journalRepository.editJournal(state.selectedDate, JournalPatch(date = state.selectedDate, sleepNotes = notes))
            _state.update { it.copy(sleepNotes = result?.sleepNotes ?: notes, error = null) }
        }
    }
}

class OnboardingViewModel(private val services: AppServices, private val profileViewModel: ProfileViewModel) : ViewModel() {
    var step by mutableStateOf(OnboardingStep.Microphone)
    var privacyPolicyAgreed by mutableStateOf(false)
    var selectedSleepOption by mutableStateOf<String?>(null)

    fun initialize(profile: ProfileState) {
        step = if (profile.privacyOnboardingComplete && !profile.appOnboardingComplete) OnboardingStep.Questions else OnboardingStep.Microphone
        privacyPolicyAgreed = profile.preferences.agreedToPrivacyPolicy
    }

    fun next() { step = OnboardingStep.entries[(OnboardingStep.entries.indexOf(step) + 1).coerceAtMost(OnboardingStep.entries.lastIndex)] }
    fun back() { step = OnboardingStep.entries[(OnboardingStep.entries.indexOf(step) - 1).coerceAtLeast(0)] }

    fun setMicrophone(enabled: Boolean) {
        viewModelScope.launch {
            val granted = !enabled || services.audioGateway.requestPermission()
            val profile = profileViewModel.state.value.preferences
            profileViewModel.setPreferences(profile.copy(microphoneEnabled = granted && enabled))
        }
    }

    fun finishPrivacy(navController: NavHostController) {
        val updated = profileViewModel.state.value.preferences.copy(agreedToPrivacyPolicy = privacyPolicyAgreed)
        profileViewModel.setPreferences(updated)
        profileViewModel.completePrivacy { step = OnboardingStep.QuestionsIntro }
    }

    fun finishQuestions(navController: NavHostController) {
        viewModelScope.launch {
            selectedSleepOption?.let {
                services.generalSleepRepository.createSleepData(GeneralSleepData("", it, "", "", ""))
            }
            profileViewModel.completeApp {
                navController.navigate(Routes.Sleep) { popUpTo(Routes.Onboarding) { inclusive = true } }
            }
        }
    }
}

enum class OnboardingStep {
    Microphone, Accelerometer, LightSensor, JournalData, CloudStorage, PrivacyAgreement, Transparency, QuestionsIntro, Questions
}

object Routes {
    const val Auth = "auth"
    const val Onboarding = "onboarding"
    const val Sleep = "sleep"
    const val SleepMode = "sleepMode"
    const val Journal = "journal"
    const val Statistics = "statistics"
    const val Profile = "profile"
    const val Consent = "consent"
    const val PrivacyPolicy = "privacyPolicy"
}

@Composable
fun SleepTrackerApp(services: AppServices) {
    // MIGRATION: The RN root layout initializes Zustand stores and redirects.
    //            Compose keeps that bootstrap in remembered ViewModels and a
    //            NavHost so state survives recomposition.
    val navController = rememberNavController()
    val authViewModel = remember { AuthViewModel(services) }
    val profileViewModel = remember { ProfileViewModel(services) }
    val transparencyViewModel = remember { TransparencyViewModel(services) }
    val sleepViewModel = remember { SleepViewModel(services) }
    val journalViewModel = remember { JournalViewModel(services) }
    val onboardingViewModel = remember { OnboardingViewModel(services, profileViewModel) }
    val auth by authViewModel.state.collectAsState()
    val profile by profileViewModel.state.collectAsState()

    val startRoute = if (!auth.isAuthenticated) Routes.Auth else if (!profile.privacyOnboardingComplete || !profile.appOnboardingComplete) Routes.Onboarding else Routes.Sleep

    MaterialTheme(typography = MaterialTheme.typography.copy(bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily(Font(Res.font.space_mono_regular))))) {
        NavHost(navController, startDestination = startRoute) {
            composable(Routes.Auth) {
                AuthScreen(authViewModel) {
                    val complete = profileViewModel.state.value.privacyOnboardingComplete && profileViewModel.state.value.appOnboardingComplete
                    navController.navigate(if (complete) Routes.Sleep else Routes.Onboarding) { popUpTo(Routes.Auth) { inclusive = true } }
                }
            }
            composable(Routes.Onboarding) {
                LaunchedEffect(Unit) { onboardingViewModel.initialize(profileViewModel.state.value) }
                OnboardingScreen(onboardingViewModel, profileViewModel, navController)
            }
            composable(Routes.Sleep) { TabScaffold(navController, Routes.Sleep) { SleepScreen(sleepViewModel, transparencyViewModel, navController) } }
            composable(Routes.SleepMode) { SleepModeScreen(sleepViewModel, transparencyViewModel, navController) }
            composable(Routes.Journal) { TabScaffold(navController, Routes.Journal) { JournalScreen(journalViewModel, transparencyViewModel) } }
            composable(Routes.Statistics) { TabScaffold(navController, Routes.Statistics) { StatisticsScreen(transparencyViewModel, navController) } }
            composable(Routes.Profile) { TabScaffold(navController, Routes.Profile) { ProfileScreen(authViewModel, navController) } }
            composable(Routes.Consent) { ConsentPreferencesScreen(profileViewModel, services, navController) }
            composable(Routes.PrivacyPolicy) { PrivacyPolicyScreen(navController) }
        }
    }
}

@Composable
fun TabScaffold(navController: NavHostController, selected: String, content: @Composable () -> Unit) {
    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            NavigationBar(containerColor = AppColors.LightBlack, contentColor = Color.White) {
                listOf(
                    Routes.Sleep to ("Sleep" to Icons.Default.Nightlight),
                    Routes.Journal to ("Journal" to Icons.Default.Description),
                    Routes.Statistics to ("Statistics" to Icons.Default.BarChart),
                    Routes.Profile to ("Profile" to Icons.Default.Person),
                ).forEach { (route, pair) ->
                    NavigationBarItem(
                        selected = selected == route,
                        onClick = { navController.navigate(route) { launchSingleTop = true } },
                        icon = { Icon(pair.second, contentDescription = pair.first) },
                        label = { Text(pair.first, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = AppColors.GeneralBlue, selectedTextColor = AppColors.GeneralBlue, unselectedIconColor = AppColors.Grey, unselectedTextColor = AppColors.Grey),
                    )
                }
            }
        },
    ) { padding -> Box(Modifier.padding(padding)) { content() } }
}

@Composable
fun AuthScreen(viewModel: AuthViewModel, onAuthenticated: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var register by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    Box(Modifier.fillMaxSize().background(Color.Black).windowInsetsPadding(WindowInsets.statusBars)) {
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 40.dp)) {
            item {
                Text(if (register) "Register Now!" else "Welcome Back!", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(if (register) "Create an account" else "Sign in to your account", color = Color.White, fontSize = 16.sp)
                Spacer(Modifier.height(40.dp))
                if (register) {
                    AuthInputField("Email", email, { email = it }, KeyboardType.Email)
                    AuthInputField("First Name", firstName, { firstName = it })
                    AuthInputField("Last Name", lastName, { lastName = it })
                    AuthInputField("Password", password, { password = it }, secure = true)
                    AuthInputField("Confirm Password", confirmPassword, { confirmPassword = it }, secure = true)
                } else {
                    AuthInputField("Email", email, { email = it }, KeyboardType.Email)
                    AuthInputField("Password", password, { password = it }, secure = true)
                }
                state.error?.let { Text(it, color = AppColors.TooltipRed, fontSize = 13.sp) }
                GeneralButton(if (state.loading) "Loading..." else if (register) "Register" else "Sign In") {
                    if (register) viewModel.register(firstName, lastName, email, password, onAuthenticated) else viewModel.login(email, password, onAuthenticated)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Text(if (register) "Do you have an account? " else "Don't have an account? ", color = Color.White, fontSize = 16.sp)
                    Text(if (register) "Sign In" else "Register", color = AppColors.HyperlinkBlue, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { register = !register })
                }
            }
        }
    }
}

@Composable
fun AuthInputField(label: String, value: String, onChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, secure: Boolean = false) {
    var visible by remember { mutableStateOf(!secure) }
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(label, color = Color(0xFF9CA3AF)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        visualTransformation = if (secure && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (secure) ({ IconButton({ visible = !visible }) { Icon(if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = AppColors.InputFieldPlaceholder) } }) else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(focusedContainerColor = AppColors.InputFieldBackground, unfocusedContainerColor = AppColors.InputFieldBackground, focusedIndicatorColor = AppColors.InputFieldSelected, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = Color.White, unfocusedTextColor = Color.White),
        shape = RoundedCornerShape(8.dp),
    )
}

@Composable
fun GeneralButton(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AppColors.GeneralBlue, contentColor = AppColors.LightBlack),
        shape = RoundedCornerShape(8.dp),
    ) { Text(label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
}

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, profileViewModel: ProfileViewModel, navController: NavHostController) {
    val profile by profileViewModel.state.collectAsState()
    when (viewModel.step) {
        OnboardingStep.Microphone -> OnboardingPermissionPage(
            image = Res.drawable.microphone_bg,
            showBack = false,
            purpose = "Your microphone will listen for sounds like snoring or sleep talking only while you are sleeping. Analyzing these sounds will help you detect potential sleep disruptions and get a clearer picture of your sleep environment.",
            linkText = "Read more about sound data and snoring detection",
            sectionId = "microphone",
            value = profile.preferences.microphoneEnabled,
            label = "Yes, you have permission to access my microphone to record my sleep sounds.",
            onBack = viewModel::back,
            onChange = viewModel::setMicrophone,
            onContinue = viewModel::next,
        )
        OnboardingStep.Accelerometer -> OnboardingPermissionPage(
            image = Res.drawable.running_bg,
            showBack = true,
            purpose = "The accelerometer on your device will be used to track your body movements during sleep and throughout the day continuously in the background. This will help us to correlate activity levels with sleep quality.",
            linkText = "More about collecting activity data",
            sectionId = "accelerometer",
            value = profile.preferences.accelerometerEnabled,
            label = "Yes, you have my permission to access my accelerometer to track my activity levels.",
            onBack = viewModel::back,
            onChange = { profileViewModel.setAccelerometerEnabled(it) },
            onContinue = viewModel::next,
        )
        OnboardingStep.LightSensor -> OnboardingPermissionPage(
            image = Res.drawable.bedroom_light_bg,
            showBack = true,
            purpose = "The ambient light sensor on your device will be used to monitor the light conditions in your sleep environment only while you are sleeping, helping us to understand how light exposure affects your sleep quality.",
            linkText = "More about collecting ambient light data",
            sectionId = "lightSensor",
            value = profile.preferences.lightSensorEnabled,
            label = "Yes, you have my permission to access my light sensor to track ambient light levels.",
            onBack = viewModel::back,
            onChange = { profileViewModel.setPreferences(profile.preferences.copy(lightSensorEnabled = it)) },
            onContinue = viewModel::next,
        )
        OnboardingStep.JournalData -> OnboardingInfoImagePage(Res.drawable.journal_bg, viewModel::back, viewModel::next)
        OnboardingStep.CloudStorage -> OnboardingCloudStoragePage(profile.preferences, profileViewModel, viewModel::back, viewModel::next)
        OnboardingStep.PrivacyAgreement -> OnboardingPrivacyAgreementPage(viewModel, viewModel::back) { viewModel.next() }
        OnboardingStep.Transparency -> OnboardingTransparencyPage(viewModel::back) { viewModel.finishPrivacy(navController) }
        OnboardingStep.QuestionsIntro -> QuestionsIntroPage(viewModel::back, viewModel::next)
        OnboardingStep.Questions -> QuestionsPage(viewModel, navController)
    }
}

@Composable
fun OnboardingHeader(title: String, onBack: (() -> Unit)? = null) {
    Row(Modifier.fillMaxWidth().padding(top = 60.dp, bottom = 20.dp, start = 20.dp, end = 20.dp).heightIn(min = 40.dp), verticalAlignment = Alignment.CenterVertically) {
        if (onBack != null) IconButton(onBack, modifier = Modifier.size(40.dp)) { Icon(Icons.Default.ChevronLeft, null, tint = AppColors.GeneralBlue) }
        Text(title, color = Color.White, fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = if (onBack == null) TextAlign.Center else TextAlign.Start)
    }
}

@Composable
fun OnboardingPermissionPage(image: DrawableResource, showBack: Boolean, purpose: String, linkText: String, sectionId: String, value: Boolean, label: String, onBack: () -> Unit, onChange: (Boolean) -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        Box(Modifier.weight(1.6f).fillMaxWidth()) {
            Image(painterResource(image), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            OnboardingHeader("Your Privacy Matters to Us", if (showBack) onBack else null)
        }
        OnboardingBottomPanel(Modifier.weight(5.4f)) {
            Text("Purpose:", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(purpose, color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
            LinkText(linkText, sectionId)
            PermissionsToggle(value, onChange, label, horizontalPadding = 0.dp)
            GeneralButton("Continue", onClick = onContinue)
        }
    }
}

@Composable
fun OnboardingBottomPanel(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier.background(Color.Black).padding(horizontal = 24.dp)) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            item { Column { content() } }
        }
    }
}

@Composable
fun LinkText(text: String, sectionId: String, fontSize: Int = 14) {
    Text(text, color = AppColors.HyperlinkBlue, fontSize = fontSize.sp, textDecoration = TextDecoration.Underline, modifier = Modifier.padding(bottom = 32.dp))
}

@Composable
fun PermissionsToggle(value: Boolean, onChange: (Boolean) -> Unit, label: String, horizontalPadding: androidx.compose.ui.unit.Dp = 20.dp) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                // MIGRATION: React Native TouchableOpacity made the consent row
                //            easy to tap. Compose's Switch target is smaller, so
                //            the full row also toggles the permission value.
                onChange(!value)
            }
            .padding(horizontal = horizontalPadding, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = Color.White, fontSize = 16.sp, modifier = Modifier.weight(1f).padding(end = 10.dp))
        Switch(value, onChange, colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF4CAF50), checkedThumbColor = Color.White, uncheckedTrackColor = Color(0xFFCCCCCC), uncheckedThumbColor = Color.White))
    }
}

@Composable
fun OnboardingInfoImagePage(image: DrawableResource, onBack: () -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        Box(Modifier.weight(3f).fillMaxWidth()) {
            Image(painterResource(image), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            OnboardingHeader("Your Privacy Matters to Us", onBack)
        }
        OnboardingBottomPanel(Modifier.weight(6f)) {
            Text("Journal Data:", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Information about your mood, habits, symptoms can help us correlate your personal experiences with your sleep patterns. You can voluntarily provide us with this data by making diary entries and sleep notes in the app's Journal section.", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
            LinkText("More about collecting journal data", "journalData")
            Text("Derived Data:", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("The app will derive data about you such as sleep quality, correlations, insights and recommendations. This will be treated as sensitive personal health information.", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
            LinkText("More about derived data", "derivedData")
            GeneralButton("Continue", onClick = onContinue)
        }
    }
}

@Composable
fun OnboardingCloudStoragePage(preferences: UserConsentPreferences, profileViewModel: ProfileViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        OnboardingHeader("Your Privacy Matters to Us", onBack)
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp)) {
            item {
                Text("Data Storage", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("By default all of your personal health information (data collected and derived data) will be stored on your mobile device. If you opt in, we will store your personal health information in the cloud, allowing us to provide more complex sleep analysis. All data will be encrypted while in storage and when it is being transmitted.", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                PermissionsToggle(preferences.cloudStorageEnabled, { profileViewModel.setPreferences(preferences.copy(cloudStorageEnabled = it)) }, "Yes, you have my permission to store my personal health information on secure Google Cloud servers", 0.dp)
                Text("Data Access:", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("We are committed to strict limitations on data sharing. We do not give your personal information to any third parties for marketing, advertising, or any other commercial purposes.", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                LinkText("More about data storage and data access", "cloudVsLocalStorage")
                GeneralButton("Continue", onClick = onContinue)
            }
        }
    }
}

@Composable
fun OnboardingPrivacyAgreementPage(viewModel: OnboardingViewModel, onBack: () -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        OnboardingHeader("Your Privacy Matters to Us", onBack)
        Column(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp)) {
            Text("The previous screens explained the most important parts of the privacy policy.\nBefore you proceed, please review the full Privacy Policy to understand in greater detail how we collect, use, and protect your health data.", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(bottom = 24.dp))
            LinkText("Read our full Privacy Policy", "", 16)
            Row(Modifier.clickable { viewModel.privacyPolicyAgreed = !viewModel.privacyPolicyAgreed }, verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)).border(2.dp, AppColors.GeneralBlue, RoundedCornerShape(6.dp)).background(if (viewModel.privacyPolicyAgreed) AppColors.GeneralBlue else Color.Transparent), contentAlignment = Alignment.Center) {
                    if (viewModel.privacyPolicyAgreed) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Text("I have read and agree to the Privacy Policy.", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(start = 12.dp))
            }
            Spacer(Modifier.height(24.dp))
            GeneralButton("Continue", enabled = viewModel.privacyPolicyAgreed, onClick = onContinue)
        }
    }
}

@Composable
fun OnboardingTransparencyPage(onBack: () -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        OnboardingHeader("Your Privacy Matters to Us", onBack)
        LazyColumn(Modifier.weight(1f).padding(horizontal = 24.dp, vertical = 32.dp)) {
            item {
                Text("Privacy Features In this App", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                Text("This prototype app is designed to prioritize transparency by embedding details about data collection within the UI. Our real-time privacy analysis system monitors data collection and provides instant visual feedback through dynamic privacy icons.", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(bottom = 20.dp))
                Text("Key Features:", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 12.dp))
                Bullet("Tooltip System:", " Click privacy icons next to data types for contextual information")
                Bullet("Privacy Pages:", " Transform entire screens to show comprehensive privacy details")
                Bullet("Real-time Analysis:", " AI-powered system detects and explains privacy risks as they occur")
                Spacer(Modifier.height(24.dp))
                Text("Privacy Risk Indicators", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
                    PrivacyRiskExample(Res.drawable.privacy_high, "Major Risk", "Policy violations, unauthorized collection")
                    PrivacyRiskExample(Res.drawable.privacy_medium, "Medium Risk", "Suboptimal practices, vague purposes")
                    PrivacyRiskExample(Res.drawable.privacy_low, "Low Risk", "Compliant, secure data handling. You will see this by default")
                }
                Spacer(Modifier.height(24.dp))
                Text("Sensor Data Icons", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                Text("Below are examples of icons used to convey sensor data privacy risks:", color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            }
        }
        Box(Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) { GeneralButton("Continue", onClick = onContinue) }
    }
}

@Composable
fun Bullet(bold: String, text: String) {
    Row(Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.Top) {
        Text("• ", color = AppColors.GeneralBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(buildAnnotatedString {
            pushStyle(SpanStyle(fontWeight = FontWeight.SemiBold)); append(bold); pop(); append(text)
        }, color = Color.White, fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
fun PrivacyRiskExample(icon: DrawableResource, label: String, description: String) {
    Column(Modifier.width(100.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(icon), null, Modifier.size(40.dp))
        Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
        Text(description, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 14.sp)
    }
}

@Composable
fun QuestionsIntroPage(onBack: () -> Unit, onContinue: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        OnboardingHeader("Your Privacy Matters to Us", onBack)
        Column(Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
            Text("Help us understand your current sleep quality", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Text("The next few screens will ask you questions about your current sleep quality and sleep habits. This will help us understand your sleep better and provide personalized insights.", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
            Text("Since this data is also personal health information, it will be encrypted and stored in your device (otherwise the cloud if you opted in)", color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
            GeneralButton("Continue", onClick = onContinue)
        }
    }
}

@Composable
fun QuestionsPage(viewModel: OnboardingViewModel, navController: NavHostController) {
    Column(Modifier.fillMaxSize().background(Color.Black).padding(horizontal = 24.dp, vertical = 20.dp), verticalArrangement = Arrangement.SpaceBetween) {
        OnboardingHeader("", viewModel::back)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("How much sleep do you usually get at night?", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 32.dp))
            listOf("6 hours or less", "6 - 8 hours", "8 - 10 hours").forEach { option ->
                OnboardingQuestionOption(option, viewModel.selectedSleepOption == option) { viewModel.selectedSleepOption = option }
            }
        }
        GeneralButton("Continue") { viewModel.finishQuestions(navController) }
    }
}

@Composable
fun OnboardingQuestionOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(bottom = 16.dp).clip(RoundedCornerShape(15.dp)).border(1.dp, AppColors.GeneralBlue, RoundedCornerShape(15.dp)).background(if (selected) AppColors.GeneralBlue else Color.Transparent).clickable(onClick = onClick).padding(horizontal = 20.dp, vertical = 18.dp)) {
        Text(label, color = if (selected) Color.White else AppColors.GeneralBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        if (selected) Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun SleepScreen(viewModel: SleepViewModel, transparencyViewModel: TransparencyViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    val transparency by transparencyViewModel.state.collectAsState()
    var normal by remember { mutableStateOf(true) }
    var timeDialog by remember { mutableStateOf<String?>(null) }
    var missingTimeDialog by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(Color.Black).windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 28.dp, bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Sleep Tracker", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            PrivacyTooltip(transparency.journal)
        }
        if (normal || TransparencyUiConfig.sleepPageTooltipEnabled) {
            NormalSleepPage(state.bedtime.ifBlank { "Set Time" }, state.alarm.ifBlank { "Set Time" }, { timeDialog = "bedtime" }, { timeDialog = "alarm" }) {
                // MIGRATION: RN blocks sleep mode until both values are set.
                //            Compose uses the same guard before navigation.
                if (state.bedtime.isBlank() || state.alarm.isBlank()) {
                    missingTimeDialog = true
                } else {
                    navController.navigate(Routes.SleepMode)
                }
            }
        } else PrivacySleepPage()
    }
    timeDialog?.let { kind ->
        TimeDialog("Set ${if (kind == "bedtime") "Bedtime" else "Alarm"}", if (kind == "bedtime") state.bedtime else state.alarm, onDismiss = { timeDialog = null }) { value ->
            if (kind == "bedtime") viewModel.saveBedtime(value) else viewModel.saveAlarm(value)
            timeDialog = null
        }
    }
    if (missingTimeDialog) {
        AlertDialog(
            onDismissRequest = { missingTimeDialog = false },
            containerColor = AppColors.LightBlack,
            title = { Text("Missing Information", color = Color.White) },
            text = { Text("Please set your Bedtime and Alarm before starting sleep mode.", color = Color.White) },
            confirmButton = { TextButton({ missingTimeDialog = false }) { Text("OK", color = AppColors.GeneralBlue) } },
        )
    }
}

@Composable
fun NormalSleepPage(bedtime: String, alarm: String, onBedtime: () -> Unit, onAlarm: () -> Unit, onStart: () -> Unit) {
    LazyColumn {
        item {
            Image(painterResource(Res.drawable.sleep_duration_wheel), null, Modifier.fillMaxWidth().aspectRatio(1f).padding(bottom = 30.dp), contentScale = ContentScale.Fit)
            TimeCard("Bedtime", bedtime, onBedtime)
            TimeCard("Alarm", alarm, onAlarm)
            Button(onClick = onStart, colors = ButtonDefaults.buttonColors(containerColor = AppColors.GeneralBlue), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 30.dp).height(58.dp)) {
                Text("SLEEP NOW", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TimeCard(label: String, value: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(bottom = 15.dp).clip(RoundedCornerShape(12.dp)).background(AppColors.LightBlack).clickable(onClick = onClick).padding(horizontal = 20.dp, vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text(value, color = Color.White.copy(alpha = .8f), fontSize = 18.sp, modifier = Modifier.padding(end = 10.dp))
        Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun SleepModeScreen(sleepViewModel: SleepViewModel, transparencyViewModel: TransparencyViewModel, navController: NavHostController) {
    val sleep by sleepViewModel.state.collectAsState()
    val transparency by transparencyViewModel.state.collectAsState()
    var progress by remember { mutableStateOf(0f) }
    var displayNormalUI by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) { delay(1000); progress = progress }
    }
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        Image(painterResource(Res.drawable.sleep_mode_bg), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        if (displayNormalUI || TransparencyUiConfig.sleepModeTooltipEnabled) {
            Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(currentClockText(), color = Color.White, fontSize = 64.sp, fontWeight = FontWeight.Bold)
                Text("Alarm ${sleep.alarm.ifBlank { "--" }}", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(top = 20.dp, bottom = 40.dp))
                Button(onClick = { navController.navigate(Routes.Statistics) { popUpTo(Routes.SleepMode) { inclusive = true } } }, colors = ButtonDefaults.buttonColors(containerColor = AppColors.GeneralBlue), shape = CircleShape, modifier = Modifier.size(160.dp)) {
                    Text("Hold\nWake Up", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            PrivacySleepModePage(transparency)
        }
        if (TransparencyUiConfig.sleepModeTooltipEnabled) {
            // MIGRATION: React Native sleep mode renders three independent
            //            sensor transparency icons over the background. Compose
            //            keeps them as overlay content in the Box so they do not
            //            affect the centered clock/alarm/wake-up layout.
            SleepModeTooltipCluster(
                transparency = transparency,
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 50.dp, end = 30.dp),
            )
        } else {
            // MIGRATION: The source app supports a non-tooltip privacy-page
            //            mode behind the transparencyConfig toggle. This keeps
            //            that demo-mode path alive instead of hard-coding the
            //            tooltip-only UI.
            Box(Modifier.align(Alignment.TopEnd).padding(top = 50.dp, end = 30.dp)) {
                PrivacyIcon(getPrivacyRiskIconForPage(listOf(transparency.accelerometer.privacyRisk, transparency.light.privacyRisk, transparency.microphone.privacyRisk)), !displayNormalUI) {
                    displayNormalUI = !displayNormalUI
                }
            }
        }
    }
}

enum class SensorPrivacyType { Accelerometer, Light, Microphone }

@Composable
fun SleepModeTooltipCluster(transparency: TransparencyState, modifier: Modifier = Modifier) {
    Column(modifier.width(260.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            SensorPrivacyTooltip(SensorPrivacyType.Accelerometer, transparency.accelerometer)
            SensorPrivacyTooltip(SensorPrivacyType.Light, transparency.light)
        }
        Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.Center) {
            SensorPrivacyTooltip(SensorPrivacyType.Microphone, transparency.microphone)
        }
    }
}

@Composable
fun SensorPrivacyTooltip(sensorType: SensorPrivacyType, event: TransparencyEvent) {
    val isCloud = event.storageLocation == DataDestination.GoogleCloud
    val width = if (sensorType == SensorPrivacyType.Accelerometer) 121.dp else 124.dp
    val height = if (isCloud) 36.dp else 45.dp
    // MIGRATION: RN used SensorPrivacyIcon assets for sleep-mode sensor
    //            transparency. Compose uses the same bitmap assets and sizes
    //            from the source component so Android renders the same visual
    //            status before the tooltip opens.
    PrivacyTooltipHost(
        event = event,
        containerModifier = Modifier.width(width).height(height),
    ) { modifier ->
        Image(
            painterResource(sensorPrivacyDrawable(sensorType, event)),
            null,
            modifier.width(width).height(height),
        )
    }
}

fun sensorPrivacyDrawable(sensorType: SensorPrivacyType, event: TransparencyEvent): DrawableResource {
    val isCloud = event.storageLocation == DataDestination.GoogleCloud
    return when (sensorType) {
        SensorPrivacyType.Accelerometer -> when (event.privacyRisk) {
            PrivacyRisk.High -> if (isCloud) Res.drawable.accelerometer_cloud_high else Res.drawable.accelerometer_local_high
            PrivacyRisk.Medium -> if (isCloud) Res.drawable.accelerometer_cloud_medium else Res.drawable.accelerometer_local_medium
            PrivacyRisk.Low -> if (isCloud) Res.drawable.accelerometer_cloud_low else Res.drawable.accelerometer_local_low
        }
        SensorPrivacyType.Light -> when (event.privacyRisk) {
            PrivacyRisk.High -> if (isCloud) Res.drawable.light_cloud_high else Res.drawable.light_local_high
            PrivacyRisk.Medium -> if (isCloud) Res.drawable.light_cloud_medium else Res.drawable.light_local_medium
            PrivacyRisk.Low -> if (isCloud) Res.drawable.light_cloud_low else Res.drawable.light_local_low
        }
        SensorPrivacyType.Microphone -> when (event.privacyRisk) {
            PrivacyRisk.High -> if (isCloud) Res.drawable.microphone_cloud_high else Res.drawable.microphone_local_high
            PrivacyRisk.Medium -> if (isCloud) Res.drawable.microphone_cloud_medium else Res.drawable.microphone_local_medium
            PrivacyRisk.Low -> if (isCloud) Res.drawable.microphone_cloud_low else Res.drawable.microphone_local_low
        }
    }
}

@Composable
fun PrivacySleepModePage(transparency: TransparencyState) {
    val sensors = listOf(
        "Accelerometer" to transparency.accelerometer,
        "Light Sensor" to transparency.light,
        "Microphone" to transparency.microphone,
    ).sortedByDescending { privacyRiskSortOrder(it.second.privacyRisk) }
    LazyColumn(Modifier.fillMaxSize().padding(top = 110.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)) {
        items(sensors) { sensor ->
            SensorPrivacyDetailsCard(sensor.first, sensor.second)
        }
    }
}

@Composable
fun SensorPrivacyDetailsCard(title: String, event: TransparencyEvent) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(0.5.dp, Color.White.copy(alpha = .1f), RoundedCornerShape(8.dp))
            .background(AppColors.LightBlack)
            .padding(12.dp),
    ) {
        Text(title, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(getPrivacyRiskLabel(event.privacyRisk), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp))
        Text(event.aiExplanation.why, color = Color.White.copy(alpha = .9f), fontSize = 13.sp, lineHeight = 17.sp, modifier = Modifier.padding(top = 8.dp))
    }
}

fun privacyRiskSortOrder(risk: PrivacyRisk): Int = when (risk) {
    PrivacyRisk.High -> 2
    PrivacyRisk.Medium -> 1
    PrivacyRisk.Low -> 0
}

@Composable
fun JournalScreen(viewModel: JournalViewModel, transparencyViewModel: TransparencyViewModel) {
    val state by viewModel.state.collectAsState()
    val transparency by transparencyViewModel.state.collectAsState()
    var normal by remember { mutableStateOf(true) }
    var editDiary by remember { mutableStateOf(false) }
    var editNotes by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        HeaderImage {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column { Text("Today", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.SemiBold); Text(formatMonthDay(todayInstant()), color = Color.White, fontSize = 18.sp) }
                if (!TransparencyUiConfig.journalTooltipEnabled) PrivacyIcon(getPrivacyRiskIconForPage(listOf(transparency.journal.privacyRisk, transparency.accelerometer.privacyRisk)), false) { normal = !normal }
            }
            CalendarStrip(state.selectedDate) { viewModel.load(it) }
        }
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            item {
                state.error?.let { Text(it, color = AppColors.TooltipRed) }
                if (normal) NormalJournalPage(state, transparency, { editDiary = true }, { editNotes = true }) else PrivacyJournalPage()
            }
        }
    }
    if (editDiary) JournalEntryDialog(state.diaryEntry, { editDiary = false }) { viewModel.saveDiary(it); editDiary = false }
    if (editNotes) SleepNotesDialog(state.sleepNotes, { editNotes = false }) { viewModel.saveNotes(it); editNotes = false }
}

@Composable
fun HeaderImage(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxWidth().height(220.dp).padding(bottom = 20.dp).clip(RoundedCornerShape(16.dp))) {
        Image(painterResource(Res.drawable.journal_bg), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        Box(Modifier.fillMaxSize().background(Color(0xCC001428)))
        Column(Modifier.padding(start = 30.dp, end = 30.dp, top = 50.dp, bottom = 20.dp)) { content() }
    }
}

@Composable
fun CalendarStrip(selectedDate: String, onSelected: (String) -> Unit) {
    LazyRow(Modifier.fillMaxWidth().height(72.dp).padding(top = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items((-3..3).map { offsetDate(it) }) { date ->
            Box(Modifier.size(44.dp).clip(RoundedCornerShape(22.dp)).background(if (date == selectedDate) AppColors.GeneralBlue else Color.Transparent).clickable { onSelected(date) }, contentAlignment = Alignment.Center) {
                Text(date.takeLast(2), color = Color.White)
            }
        }
    }
}

@Composable
fun NormalJournalPage(state: JournalState, transparency: TransparencyState, onDiary: () -> Unit, onNotes: () -> Unit) {
    Text("Sleep Goal", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp))
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AppColors.LightBlack).padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Column { JournalMetric(Icons.Default.Nightlight, "Bedtime", state.bedtime); Spacer(Modifier.height(15.dp)); JournalMetric(Icons.Default.Alarm, "Alarm", state.alarm) }
        JournalMetric(Icons.Default.Explore, "Goal", state.sleepGoal, alignEnd = true)
    }
    SectionTitleWithTooltip("Diary", transparency.journal)
    CardBox {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Sleep Notes", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            IconButton(onNotes) { Icon(Icons.Default.AddCircleOutline, null, tint = AppColors.GeneralBlue) }
        }
        if (state.sleepNotes.isEmpty()) Text("No sleep notes added yet.", color = Color(0xFF8E8E93), fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
        state.sleepNotes.forEach { Text("• ${it.label}", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(bottom = 5.dp)) }
    }
    Row(Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(16.dp)).background(AppColors.LightBlack), verticalAlignment = Alignment.CenterVertically) {
        Text(state.diaryEntry.ifBlank { "Write something to record your day... " }, color = Color.White.copy(alpha = .8f), fontSize = 16.sp, modifier = Modifier.weight(1f).padding(horizontal = 20.dp, vertical = 15.dp))
        IconButton(onDiary) { Icon(Icons.Default.Edit, null, tint = Color.White) }
    }
    SectionTitleWithTooltip("Activity Tracker", transparency.accelerometer)
    CardBox {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
            ActivityProgress("Steps", "steps", Modifier.weight(1f))
            ActivityProgress("Calories", "kcal", Modifier.weight(1f))
        }
    }
}

@Composable
fun JournalMetric(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String, alignEnd: Boolean = false) {
    Column(horizontalAlignment = if (alignEnd) Alignment.End else Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = Color.White.copy(alpha = .7f), modifier = Modifier.size(16.dp)); Text(" $label", color = Color.White.copy(alpha = .7f), fontSize = 14.sp) }
        Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SectionTitleWithTooltip(title: String, event: TransparencyEvent) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 15.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        if (TransparencyUiConfig.journalTooltipEnabled) PrivacyTooltip(event)
    }
}

@Composable
fun CardBox(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth().padding(bottom = 15.dp).clip(RoundedCornerShape(16.dp)).background(AppColors.LightBlack).padding(20.dp), content = content)
}

@Composable
fun ActivityProgress(label: String, unit: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Box(Modifier.padding(top = 10.dp).size(80.dp).clip(CircleShape).border(3.dp, AppColors.GeneralBlue, CircleShape).background(Color.White.copy(alpha = .1f)), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("83", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold); Text(unit, color = Color.White.copy(alpha = .7f), fontSize = 12.sp) }
        }
    }
}

@Composable
fun StatisticsScreen(transparencyViewModel: TransparencyViewModel, navController: NavHostController) {
    var daily by remember { mutableStateOf(true) }
    var normal by remember { mutableStateOf(true) }
    var selectedDate by remember { mutableStateOf(todayIso()) }
    val transparency by transparencyViewModel.state.collectAsState()
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        HeaderImage {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row { TabPill("Daily", daily) { daily = true; normal = true }; Spacer(Modifier.width(10.dp)); TabPill("Statistics", !daily) { daily = false; normal = true } }
                PrivacyIcon(getPrivacyRiskIcon(transparency.statistics.privacyRisk), !normal) { normal = !normal }
            }
            if (daily) StatisticsWeeklyCalendar(selectedDate) { date ->
                // MIGRATION: React Native kept selectedDate in component state
                //            and re-rendered the static prototype statistics
                //            when a calendar day was pressed. KMP mirrors that
                //            stateful highlight instead of passing todayIso()
                //            as an immutable value.
                selectedDate = date
            }
        }
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            item {
                if (!normal) PrivacyStatisticsPage(transparency.statistics, navController) else if (daily) DailyStatisticsPage() else {
                    StatisticItem("Sleep Quality", Res.drawable.sleep_quality_graph)
                    StatisticItem("Sleep Duration", Res.drawable.sleep_duration_graph)
                    StatisticItem("Sleep Stages", Res.drawable.sleep_duration_graph)
                    StatisticItem("Snore Time", Res.drawable.sleep_quality_graph)
                }
            }
        }
    }
}

@Composable
fun StatisticsWeeklyCalendar(selectedDate: String, onSelected: (String) -> Unit) {
    val days = remember(selectedDate) { weekDatesFor(selectedDate) }
    val weekDays = listOf("S", "M", "T", "W", "T", "F", "S")
    Column(Modifier.fillMaxWidth().padding(top = 12.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            weekDays.forEach { day ->
                Text(day, color = Color.White.copy(alpha = .7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.width(35.dp))
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = 15.dp), horizontalArrangement = Arrangement.SpaceAround) {
            days.forEach { date ->
                val selected = date == selectedDate
                Box(
                    Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(if (selected) Color.White else Color.Transparent)
                        .clickable { onSelected(date) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        parseIsoDateOrToday(date).dayOfMonth.toString(),
                        color = if (selected) Color(0xFF001122) else Color.White,
                        fontSize = 16.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
fun TabPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Text(label, color = if (selected) Color.White else AppColors.LightGrey, fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(if (selected) AppColors.GeneralBlue else Color.Transparent).clickable(onClick = onClick).padding(horizontal = 20.dp, vertical = 10.dp))
}

@Composable
fun DailyStatisticsPage() {
    Text("Sleep Quality", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp))
    CardBox {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(Res.drawable.sleep_quality_daily), null, Modifier.size(100.dp).clip(RoundedCornerShape(40.dp)), contentScale = ContentScale.Crop)
            Column(Modifier.padding(start = 20.dp)) {
                Text("Time in Bed", color = Color(0xFF888888), fontSize = 14.sp)
                Text("10:14 PM - 6:44 AM", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text("8h 30m", color = AppColors.LightGrey, fontSize = 14.sp)
                Text("Pretty Good!", color = AppColors.GeneralBlue, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
    StatisticItem("Sleep Stages", Res.drawable.sleep_stages_daily)
    StageGrid()
    InsightGrid()
    Text("Sleep Clips", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp))
    SleepClipsCard()
}

@Composable
fun StatisticItem(label: String, image: DrawableResource) {
    Text(label, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 15.dp))
    Box(Modifier.fillMaxWidth().padding(bottom = 20.dp).clip(RoundedCornerShape(15.dp)).background(AppColors.LightBlack).padding(20.dp)) {
        Image(painterResource(image), null, Modifier.fillMaxWidth().height(200.dp), contentScale = ContentScale.Fit)
    }
}

@Composable
fun StageGrid() {
    Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        StageItem("Deep Sleep", "21%", "2h 25m", Icons.Default.Nightlight, Color(0xFF4A4A4A))
        StageItem("Light Sleep", "56%", "4h 35m", Icons.Default.Nightlight, Color(0xFF6A9EFF))
        StageItem("REM", "17%", "1h 25m", Icons.Default.Visibility, Color(0xFF8A6AFF))
        StageItem("Awake", "6%", "30m", Icons.Default.RemoveRedEye, Color(0xFFFFA64A))
    }
}

@Composable
fun StageItem(label: String, percent: String, duration: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Column(Modifier.width(76.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) { Icon(icon, null, tint = Color.White, modifier = Modifier.size(16.dp)) }
        Text(label, color = AppColors.LightGrey, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
        Text(percent, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Text(duration, color = AppColors.LightGrey, fontSize = 12.sp)
    }
}

@Composable
fun InsightGrid() {
    Column(Modifier.padding(bottom = 20.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InsightItem(Icons.Default.Bed, Color(0xFF4A9EFF), "In Bed", "8h 30 min", Modifier.weight(1f))
            InsightItem(Icons.Default.Nightlight, Color(0xFF8A6AFF), "Asleep", "7h 34 min", Modifier.weight(1f))
            InsightItem(Icons.Default.Alarm, Color(0xFF6A9EFF), "Asleep After", "11 min", Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InsightItem(Icons.Default.VolumeUp, Color(0xFFFFA64A), "Noise", "39 dB", Modifier.weight(1f))
            InsightItem(Icons.Default.VolumeUp, Color(0xFFFF6B6B), "Snoring", "1h 30 min", Modifier.weight(1f))
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
fun InsightItem(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, label: String, value: String, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(AppColors.LightBlack).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        Text(label, color = AppColors.LightGrey, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    }
}

@Composable
fun SleepClipsCard() {
    var snoring by remember { mutableStateOf(true) }
    CardBox {
        Row { ClipTab("Snoring", snoring) { snoring = true }; Spacer(Modifier.width(10.dp)); ClipTab("Talking", !snoring) { snoring = false } }
        repeat(3) { ClipListItem() }
    }
}

@Composable
fun ClipTab(label: String, selected: Boolean, onClick: () -> Unit) {
    Text(label, color = if (selected) Color.White else AppColors.LightGrey, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 20.dp, bottom = 12.dp).clip(RoundedCornerShape(16.dp)).background(if (selected) AppColors.GeneralBlue else Color(0xFF333333)).clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp))
}

@Composable
fun ClipListItem() {
    Row(Modifier.fillMaxWidth().padding(top = 12.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF333333)).padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.PlayCircleOutline, null, tint = Color(0xFF4A9EFF))
        Text("11:04 PM", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 12.dp).width(60.dp))
        Row(Modifier.weight(1f).height(24.dp), verticalAlignment = Alignment.CenterVertically) { listOf(8, 18, 11, 24, 15, 9, 22, 14, 19, 7, 25, 13, 17, 10, 21).forEach { Box(Modifier.padding(end = 2.dp).width(2.dp).height(it.dp).background(AppColors.GeneralBlue)) } }
        Icon(Icons.Default.MoreHoriz, null, tint = Color(0xFF888888))
    }
}

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, navController: NavHostController) {
    val user by authViewModel.state.collectAsState()
    Column(Modifier.fillMaxSize().background(Color.Black).windowInsetsPadding(WindowInsets.statusBars).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Text("Profile", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text("Hello, ${user.user?.firstName?.ifBlank { "Guest" } ?: "Guest"}", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Column(Modifier.fillMaxWidth()) {
            ProfileMenuItem("Consent Preferences") { navController.navigate(Routes.Consent) }
            ProfileMenuItem("Privacy Policy") { navController.navigate(Routes.PrivacyPolicy) }
        }
        Button({ authViewModel.logout(); navController.navigate(Routes.Auth) { popUpTo(Routes.Profile) { inclusive = true } } }, colors = ButtonDefaults.buttonColors(containerColor = AppColors.GeneralBlue), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(58.dp)) {
            Text("LOGOUT", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(bottom = 15.dp).clip(RoundedCornerShape(12.dp)).background(AppColors.LightBlack).clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 28.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = AppColors.GeneralBlue, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun ConsentPreferencesScreen(profileViewModel: ProfileViewModel, services: AppServices, navController: NavHostController) {
    val profile by profileViewModel.state.collectAsState()
    val preferences = profile.preferences
    LazyColumn(Modifier.fillMaxSize().background(Color.Black)) {
        item { OnboardingHeader("Your Privacy Matters to Us") { navController.popBackStack() } }
        item {
            PermissionsToggle(preferences.microphoneEnabled, { profileViewModel.setPreferences(preferences.copy(microphoneEnabled = it)) }, "Yes, you have permission to access my microphone to record my sleep sounds.")
            LinkText("Read more about sound data and snoring detection", "microphone")
            PermissionsToggle(preferences.accelerometerEnabled, { profileViewModel.setAccelerometerEnabled(it) }, "Yes, you have my permission to access my accelerometer to track my activity levels.")
            LinkText("More about collecting activity data", "accelerometer")
            PermissionsToggle(preferences.lightSensorEnabled, { profileViewModel.setPreferences(preferences.copy(lightSensorEnabled = it)) }, "Yes, you have my permission to access my light sensor to track ambient light levels.")
            LinkText("More about collecting ambient light data", "lightSensor")
            PermissionsToggle(preferences.cloudStorageEnabled, { profileViewModel.setPreferences(preferences.copy(cloudStorageEnabled = it)) }, "Yes, you have my permission to store my personal health information on secure Google Cloud servers")
            LinkText("More about data storage and data access", "cloudVsLocalStorage")
        }
    }
}

@Composable
fun PrivacyPolicyScreen(navController: NavHostController) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val sectionIndexById = remember { privacyPolicyContent.mapIndexedNotNull { index, item -> item.id?.let { it to index } }.toMap() }
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.Black).padding(20.dp),
        state = listState,
    ) {
        item {
            OnboardingHeader("Privacy Policy") { navController.popBackStack() }
            Text("Version: 1.0.0 | Effective Date: 2025-06-10 | Last Updated: 2025-07-07", color = Color(0xFFBBBBBB), fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp))
            Text("Table of Contents", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
            privacyPolicyToc.forEach { entry ->
                Text(
                    "• ${entry.title}",
                    color = AppColors.GeneralBlue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val targetIndex = sectionIndexById[entry.sectionId] ?: return@clickable
                            scope.launch { listState.animateScrollToItem(targetIndex + 1) }
                        }
                        .padding(vertical = 4.dp, horizontal = 5.dp),
                )
            }
            Spacer(Modifier.height(12.dp))
        }
        // MIGRATION: React Native rendered this screen from
        //            privacyPolicyData.json. KMP keeps the same document in
        //            typed immutable content items so the migrated screen
        //            avoids untyped Any/dynamic maps while preserving wording.
        items(privacyPolicyContent) { item -> PrivacyPolicyContentRow(item) }
    }
}

data class PrivacyPolicyTocEntry(val title: String, val sectionId: String)

enum class PrivacyPolicyContentKind { Heading, Paragraph, Description, Bullet, Definition, SubHeading, ListItem }

data class PrivacyPolicyContentItem(
    val kind: PrivacyPolicyContentKind,
    val text: String,
    val label: String? = null,
    val level: Int = 0,
    val id: String? = null,
) {
    companion object {
        fun heading(text: String, level: Int, id: String? = null) = PrivacyPolicyContentItem(PrivacyPolicyContentKind.Heading, text, level = level, id = id)
        fun paragraph(text: String, id: String? = null) = PrivacyPolicyContentItem(PrivacyPolicyContentKind.Paragraph, text, id = id)
        fun description(text: String, id: String? = null) = PrivacyPolicyContentItem(PrivacyPolicyContentKind.Description, text, id = id)
        fun bullet(label: String, text: String, id: String? = null) = PrivacyPolicyContentItem(PrivacyPolicyContentKind.Bullet, text, label = label, id = id)
        fun definition(label: String, text: String, id: String? = null) = PrivacyPolicyContentItem(PrivacyPolicyContentKind.Definition, text, label = label, id = id)
        fun subHeading(text: String, id: String? = null) = PrivacyPolicyContentItem(PrivacyPolicyContentKind.SubHeading, text, id = id)
        fun listItem(text: String, id: String? = null) = PrivacyPolicyContentItem(PrivacyPolicyContentKind.ListItem, text, id = id)
    }
}

@Composable
fun PrivacyPolicyContentRow(item: PrivacyPolicyContentItem) {
    when (item.kind) {
        PrivacyPolicyContentKind.Heading -> {
            val fontSize = when (item.level) { 1 -> 22.sp; 2 -> 18.sp; else -> 16.sp }
            Text(
                item.text,
                color = AppColors.GeneralBlue,
                fontSize = fontSize,
                fontWeight = when (item.level) { 1 -> FontWeight.Bold; 2 -> FontWeight.SemiBold; else -> FontWeight.Medium },
                modifier = Modifier.padding(top = if (item.level == 1) 18.dp else 10.dp, bottom = 8.dp, start = when (item.level) { 1 -> 0.dp; 2 -> 5.dp; else -> 10.dp }),
            )
        }
        PrivacyPolicyContentKind.Description -> Text(item.text, color = Color.White, fontSize = 15.sp, lineHeight = 22.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, modifier = Modifier.padding(bottom = 10.dp))
        PrivacyPolicyContentKind.Bullet -> Text(
            buildAnnotatedString {
                append("• ${item.label.orEmpty()} ")
                pushStyle(SpanStyle(color = Color.White))
                append(item.text)
                pop()
            },
            color = Color(0xFFADD8E6),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(start = 15.dp, bottom = 5.dp),
        )
        PrivacyPolicyContentKind.Definition -> Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("${item.label.orEmpty()} ")
                pop()
                append(item.text)
            },
            color = Color.White,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(start = 10.dp, bottom = 8.dp),
        )
        PrivacyPolicyContentKind.SubHeading -> Text(item.text, color = Color(0xFFADD8E6), fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 3.dp))
        PrivacyPolicyContentKind.ListItem -> Text("- ${item.text}", color = Color.White, fontSize = 15.sp, modifier = Modifier.padding(start = 25.dp, bottom = 5.dp))
        PrivacyPolicyContentKind.Paragraph -> Text(item.text, color = Color.White, fontSize = 16.sp, lineHeight = 24.sp, modifier = Modifier.padding(bottom = 10.dp))
    }
}

private val privacyPolicyToc = listOf(
    PrivacyPolicyTocEntry("Interpretation and Definitions", "interpretationsAndDefinitions"),
    PrivacyPolicyTocEntry("Types of Information Collected and How We Use it", "dataCollection"),
    PrivacyPolicyTocEntry("Cloud vs. Local Data Storage & Processing", "cloudVsLocalStorage"),
    PrivacyPolicyTocEntry("Data Encryption and Pseudonymization", "dataEncryptionAndPsuedonymization"),
    PrivacyPolicyTocEntry("How We share Your information", "dataSharing"),
    PrivacyPolicyTocEntry("Retention of Your information", "dataRetention"),
    PrivacyPolicyTocEntry("Your Rights under PIPEDA", "userRights"),
    PrivacyPolicyTocEntry("Changes to the Privacy Policy", "policyChanges"),
    PrivacyPolicyTocEntry("Contact Us", "contact"),
)

private val privacyPolicyContent = listOf(
    PrivacyPolicyContentItem.paragraph("This Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your information when You use the App and tells You about Your privacy rights and how the Personal Information Protection and Electronic Documents Act (PIPEDA) protects You. We use Your Personal data to provide and improve the App.", id = "introduction"),
    PrivacyPolicyContentItem.heading("Interpretation and Definitions", level = 1, id = "interpretationsAndDefinitions"),
    PrivacyPolicyContentItem.definition("You:", "refers to the individual accessing or using the Service (Individual under PIPEDA). For the purposes of PIPEDA, You can be referred to as the Individual."),
    PrivacyPolicyContentItem.definition("Company:", "refers to Sleep Tracker Inc. (Organization under PIPEDA). For the purposes of PIPEDA, Company can be referred to as the Organization"),
    PrivacyPolicyContentItem.definition("App:", "refers to the Sleep Tracker application that is provided by the Company and downloaded by You on any mobile device "),
    PrivacyPolicyContentItem.definition("Personal Information:", "information about an identifiable individual"),
    PrivacyPolicyContentItem.definition("Personal Health Information:", "information concerning the physical or mental health of the individual or concerning any health service provided to the individual that is collected, or that is collected in the course of providing health services, or collected incidentally to the provision of health services."),
    PrivacyPolicyContentItem.heading("Types of Information Collected and How We Use Your Information", level = 1, id = "dataCollection"),
    PrivacyPolicyContentItem.paragraph("We collect various types of information to provide and improve Our App for You. The collection and use of this information are directly linked to the specific functionalities and benefits provided by the App."),
    PrivacyPolicyContentItem.heading("Personal Information", level = 2, id = "personalInformation"),
    PrivacyPolicyContentItem.description("While using Our App, We may ask You to provide Us with certain personally identifiable information that can be used to contact or identify You."),
    PrivacyPolicyContentItem.heading("Account Information", level = 3),
    PrivacyPolicyContentItem.bullet("Data Type:", "Email Address and Name"),
    PrivacyPolicyContentItem.bullet("Purpose:", "Your email address and name are collected to create and manage Your user account, enable secure login, provide essential notifications (e.g., account-related updates), and personalize Your in-app experience. This information is fundamental for Your account functionality and access to the App's services, regardless of Your cloud storage preference for Personal Health Information."),
    PrivacyPolicyContentItem.bullet("Collection Method:", "Directly from You when You register for an account."),
    PrivacyPolicyContentItem.bullet("Storage:", "Your email address and name are stored on secure Company servers located in Canada. This data is encrypted at rest using industry-standard encryption protocols (e.g., AES-256) to protect it from unauthorized access. All communication involving Your email address and name between Your device and Our servers is encrypted in transit using Transport Layer Security (TLS 1.2 or higher)."),
    PrivacyPolicyContentItem.heading("Personal Health Information", level = 2, id = "personalHealthInformation"),
    PrivacyPolicyContentItem.description("Given the nature of a sleep tracking application, We collect information that may be considered Personal Health Information under PIPEDA. "),
    PrivacyPolicyContentItem.heading("Sensor Data", level = 3, id = "sensorData"),
    PrivacyPolicyContentItem.subHeading("Microphone:", id = "microphone"),
    PrivacyPolicyContentItem.bullet("Data Type:", "Ambient sound levels"),
    PrivacyPolicyContentItem.bullet("Purpose:", "To detect and record ambient sound patterns that may indicate sleep disturbances (e.g., snoring, sleep talking). This data is used to help You identify factors impacting Your sleep environment."),
    PrivacyPolicyContentItem.bullet("Collection Method:", "Collected from Your device's microphone sensor while You are using the “Sleep” functionality of the App and explicit permission must be granted."),
    PrivacyPolicyContentItem.subHeading("Accelerometer:", id = "accelerometer"),
    PrivacyPolicyContentItem.bullet("Data Type:", "Motion and movement data"),
    PrivacyPolicyContentItem.bullet("Purpose:", "To track Your body movements during sleep, which can help infer sleep stages (e.g., light sleep, deep sleep, REM) and identify restless sleep periods. This data is used to provide You with insights into Your sleep cycles. It is also used to track Your activity levels throughout the day, allowing You to correlate activity levels to sleep quality. "),
    PrivacyPolicyContentItem.bullet("Collection Method:", "Collected from Your device's accelerometer sensor continuously in the background and explicit permission must be granted."),
    PrivacyPolicyContentItem.subHeading("Light Sensor:", id = "lightSensor"),
    PrivacyPolicyContentItem.bullet("Data Type:", "Ambient light levels"),
    PrivacyPolicyContentItem.bullet("Purpose:", "To monitor the light conditions in Your sleep environment, helping You understand how light exposure may affect Your sleep onset and quality."),
    PrivacyPolicyContentItem.bullet("Collection Method:", "Collected from Your device's light sensor while You are using the “Sleep” functionality of the App and explicit permission must be granted."),
    PrivacyPolicyContentItem.heading("Journal Data", level = 3, id = "journalData"),
    PrivacyPolicyContentItem.bullet("Data Type:", "Mood, Habits, Symptoms, Diary Entries"),
    PrivacyPolicyContentItem.bullet("Purpose:", "To allow You to record personal observations about Your daily mood, habits (e.g., caffeine intake, exercise), and any symptoms You are experiencing. This data helps You correlate Your personal experiences with Your sleep patterns and identify potential influences on Your sleep quality."),
    PrivacyPolicyContentItem.bullet("Collection Method:", "Directly from You when You voluntarily input entries into the App's journal feature."),
    PrivacyPolicyContentItem.heading("Derived Data", level = 3, id = "derivedData"),
    PrivacyPolicyContentItem.paragraph("This type of data is not collected directly from You, but The App will derive this information about You from the Personal Health Information it collects from You. This includes sleep quality scores, correlations between habits and sleep quality and personalized insights. We generate this derived data solely to provide You with more personalized and actionable insights into Your well-being, to help You understand potential correlations between different aspects of Your health, and to improve the relevance of the information and recommendations We offer within the App."),
    PrivacyPolicyContentItem.heading("Usage Data", level = 2, id = "usageData"),
    PrivacyPolicyContentItem.description("Usage Data is collected automatically when You use the App."),
    PrivacyPolicyContentItem.heading("Technical Information", level = 3),
    PrivacyPolicyContentItem.bullet("Data Type:", "Technical Information (device's IP address, device name and model, operating system name and version, unique device identifiers, App version, crash logs, time zone, system language, and country)"),
    PrivacyPolicyContentItem.bullet("Purpose:", "To monitor the overall performance and stability of the App, identify and resolve technical issues (e.g., crashes, bugs), understand user engagement with different features, and make improvements to the App's functionality."),
    PrivacyPolicyContentItem.bullet("Collection Method:", "Automatically collected from Your device and App interactions."),
    PrivacyPolicyContentItem.bullet("Storage Location:", "Usage data is stored on our secure Company servers located in Canada. "),
    PrivacyPolicyContentItem.bullet("Troubleshooting:", "For troubleshooting specific issues (e.g., why a particular feature is crashing), this data may be pseudonymized. This allows us to track patterns and resolve problems affecting Your experience without directly identifying You."),
    PrivacyPolicyContentItem.bullet("General Analytics:", "For general analytics and long-term trends (e.g., understanding the most used features or overall performance metrics), this data is anonymized by aggregating it and removing any potential identifiers, ensuring it cannot be linked back to any individual. This helps us to make broad improvements to the App while fully preserving Your privacy."),
    PrivacyPolicyContentItem.heading("Cloud vs. Local Data Storage & Processing", level = 1, id = "cloudVsLocalStorage"),
    PrivacyPolicyContentItem.paragraph("We offer You a choice regarding where Your Personal Health Information (sensor data, journal data and derived data) is stored and processed. You will be presented with a clear choice regarding cloud storage of Personal Health Information upon initial use of the App, and You will have the option to change this setting within the App's privacy settings."),
    PrivacyPolicyContentItem.heading("Cloud Storage (Opt-In)", level = 2, id = "cloudStorage"),
    PrivacyPolicyContentItem.description("By opting in to cloud storage, Your Personal Health Information will be securely stored and processed on Google Cloud servers."),
    PrivacyPolicyContentItem.bullet("Benefits:", "This option enables more complex sleep analysis, trending of Your sleep data over longer periods, and future functionalities that require significant computing resources or data synchronization across multiple devices."),
    PrivacyPolicyContentItem.bullet("Data Location:", "Please be aware that Google Cloud servers may be located in various data centers globally, including locations outside of Canada. While we choose Google Cloud for its robust security and data management capabilities, data stored outside of the Country may be subject to the laws of the jurisdiction where the servers are located. For more information on Google Cloud's data handling practices, please refer to Google Cloud's Privacy Policy."),
    PrivacyPolicyContentItem.bullet("Accountability:", "Regardless of where Your data is stored by Google Cloud, Sleep Tracker Inc. remains accountable for the protection of Your Personal Information and Personal Health Information under PIPEDA. We enter into contractual agreements with Google Cloud to ensure they provide a comparable level of protection consistent with PIPEDA principles."),
    PrivacyPolicyContentItem.heading("Local Storage (Default without Cloud Opt-In)", level = 2, id = "localStorage"),
    PrivacyPolicyContentItem.description("If You do not opt-in to cloud storage, or opt-out at any time, Your Personal Health Information will be stored primarily on Your local device."),
    PrivacyPolicyContentItem.bullet("Limitations:", "Please be aware that certain advanced or complex sleep analysis features, which require significant computing resources or data synchronization across devices, will not be available when data is stored locally."),
    PrivacyPolicyContentItem.bullet("Responsibility:", "The Company is not responsible for Personal Health Information stored solely on Your local device. If You delete the App from Your device, factory reset Your device, or if Your device is lost or damaged, all locally stored data will be permanently lost, as We do not retain copies of locally stored data on Our servers unless You have opted in for cloud storage."),
    PrivacyPolicyContentItem.bullet("Consent:", "default"),
    PrivacyPolicyContentItem.heading("Data Encryption and Pseudonymization", level = 1, id = "dataEncryptionAndPsuedonymization"),
    PrivacyPolicyContentItem.description("We employ robust security measures to protect Your data, both at rest and in transit."),
    PrivacyPolicyContentItem.heading("Encryption", level = 2, id = "encryption"),
    PrivacyPolicyContentItem.subHeading("At Rest:"),
    PrivacyPolicyContentItem.bullet("Server Data:", "All Usage Data, Personal Information and Personal Health Information stored on Google Cloud servers and Our servers is encrypted at rest using industry-standard encryption protocols (e.g., AES-256). Google Cloud implements multiple layers of encryption to protect data on its storage devices."),
    PrivacyPolicyContentItem.bullet("Local Data:", "Personal Health Information stored locally on Your device is encrypted using standard device-level encryption features available on modern mobile operating systems. This ensures that Your data is protected even if your device is compromised."),
    PrivacyPolicyContentItem.subHeading("In Transit:"),
    PrivacyPolicyContentItem.paragraph("All data transmitted between Your device, Our backend servers (for Account Information), and Google Cloud servers (for cloud-stored Personal Health Information) is encrypted using Transport Layer Security (TLS 1.2 or higher) protocols. This ensures that Your data is protected from interception during transfer."),
    PrivacyPolicyContentItem.heading("Pseudonymization", level = 2, id = "pseudonymization"),
    PrivacyPolicyContentItem.description("When Your Personal Health Information (sensor data, journal data and derived data) is transmitted to Google Cloud, it is pseudonymized by replacing direct identifiers (like Your name or email address) with unique, random identifiers. This means that while the data can be linked back to a specific user through a separate mapping held securely by the Company, it is not directly identifiable by Google Cloud or others accessing the pseudonymized data."),
    PrivacyPolicyContentItem.bullet("Purpose:", "Pseudonymization allows us to perform necessary data processing and analysis in the cloud while reducing the direct link between Your Personal Health Information and Your identity, enhancing privacy."),
    PrivacyPolicyContentItem.heading("How We Share Your Information", level = 1, id = "dataSharing"),
    PrivacyPolicyContentItem.description("We are committed to strict limitations on data sharing. We do not give Your Personal Information or Personal Health Information to any third parties for marketing, advertising, or any other commercial purposes, with the exceptions stated below. "),
    PrivacyPolicyContentItem.heading("Strictly with Google Cloud (only if opted-in)", level = 2),
    PrivacyPolicyContentItem.description("The only third-party service provider with whom Your Personal Information and Personal Health Information (Sensor Data, Journal Data and Derived Data) may be shared is Google Cloud, and only if You have explicitly opted in to cloud storage as described in the \"Cloud vs. Local Data Storage & Processing\" section. Google Cloud processes this data solely for the purpose of providing the hosting and processing services necessary for the App's functionality. We do not engage with any other third parties for analytics, advertising, payments, or any other purposes."),
    PrivacyPolicyContentItem.heading("For Legal Reasons", level = 2),
    PrivacyPolicyContentItem.description("We may disclose Your Personal Information and/or Personal Health Information where required to do so by law or in response to valid requests by public authorities (e.g., a court order or a government agency), but only to the extent necessary and in compliance with PIPEDA. "),
    PrivacyPolicyContentItem.heading("Retention of Your Information", level = 1, id = "dataRetention"),
    PrivacyPolicyContentItem.description("We retain Your Personal Information and Personal Health Information only for as long as is necessary to fulfill the purposes for which it was collected, including for satisfying any legal, accounting, or reporting requirements, and to provide you with the services you request."),
    PrivacyPolicyContentItem.heading("Account Information", level = 2),
    PrivacyPolicyContentItem.description("Your email address and name are retained as long as Your account is active. If You request to delete Your account, Your email address and name will be permanently deleted from Our systems immediately upon receiving and verifying Your request, along with all other associated data."),
    PrivacyPolicyContentItem.bullet("Data Type:", "Email Address and Name"),
    PrivacyPolicyContentItem.heading("Personal Health Information", level = 2),
    PrivacyPolicyContentItem.bullet("Cloud Stored:", "If You have opted into cloud storage and subsequently delete the App from Your device, We will retain Your cloud-stored Personal Health Information for a period of one (1) year from the date of App deletion. This is to facilitate Your potential return to the App, allowing You to resume Your sleep tracking without loss of historical data."),
    PrivacyPolicyContentItem.bullet("User Initiated Deletion:", "If You explicitly request Us to delete Your Personal Health Information from the cloud (even if Your account remains active), We will delete it immediately upon receiving and verifying Your request."),
    PrivacyPolicyContentItem.bullet("Local Stored:", "As stated above, We are not responsible for locally stored data. This data will be retained on Your device until You delete it or the App, or if Your device is compromised."),
    PrivacyPolicyContentItem.bullet("Data Type:", "Sensor Data, Journal Data and Derived Data"),
    PrivacyPolicyContentItem.heading("Usage Data", level = 2),
    PrivacyPolicyContentItem.bullet("Pseudonymized:", "Psuedonomyzed usage data will be stored on Our servers up to a maximum of two (2) years after which it will be permanently deleted. If you choose to delete Your account or ask Us to delete Your usage data, the direct link between Your account (email/name) and Your pseudonymized usage data will be immediately severed, anonymizing it. This ensures that the remaining usage data, while still useful for overall App improvement, can no longer be associated with You as an identifiable individual."),
    PrivacyPolicyContentItem.bullet("Anonymized:", "Anonymized or aggregated Usage Data may be retained indefinitely for internal analytical purposes to improve the App, as this data does not identify You personally."),
    PrivacyPolicyContentItem.heading("Your Rights under PIPEDA", level = 1, id = "userRights"),
    PrivacyPolicyContentItem.description("Under PIPEDA, You have specific rights regarding Your Personal Information. We are committed to upholding these rights"),
    PrivacyPolicyContentItem.heading("Right to Access", level = 2),
    PrivacyPolicyContentItem.paragraph("You have the right to request access to the Personal Information We hold about You. We will provide You with access to Your information within 30 days of receiving a written request."),
    PrivacyPolicyContentItem.heading("Right to Correction/Rectification", level = 2),
    PrivacyPolicyContentItem.paragraph("You have the right to request that We correct or amend any inaccurate or incomplete Personal Information We hold about You."),
    PrivacyPolicyContentItem.heading("Right to Withdraw Consent", level = 2),
    PrivacyPolicyContentItem.paragraph("You have the right to withdraw Your consent to the collection, use, and disclosure of Your Personal Information at any time, subject to legal or contractual restrictions and reasonable notice. Please note that withdrawing consent, particularly for essential data (like sensor data or cloud storage), may impact Your ability to use certain features or the entire App. For example, opting out of cloud storage will limit the availability of complex sleep analysis features."),
    PrivacyPolicyContentItem.heading("Right to Be Informed (Accountability and Openness): ", level = 2),
    PrivacyPolicyContentItem.paragraph("We are accountable for the Personal Information under Our control and will make information about Our policies and practices relating to the management of Personal Information readily available to You through this Privacy Policy and the App’s transparency features."),
    PrivacyPolicyContentItem.heading("Right to Challenge Compliance", level = 2),
    PrivacyPolicyContentItem.paragraph("You have the right to address a challenge concerning Our compliance with the above principles to Our Privacy Officer."),
    PrivacyPolicyContentItem.paragraph("To exercise any of these rights, please contact Us using the contact information provided below. We may require You to provide specific information to help Us confirm Your identity and Your right to access Your Personal Information."),
    PrivacyPolicyContentItem.heading("Data Breach Notification", level = 1, id = "dataBreachNotification"),
    PrivacyPolicyContentItem.description("In the event of a breach of security safeguards involving personal information under Our control, We have a comprehensive plan in place to respond and notify You in accordance with PIPEDA requirements."),
    PrivacyPolicyContentItem.heading("Assessment of Risk", level = 2),
    PrivacyPolicyContentItem.paragraph("Upon discovering a breach, We will immediately assess whether there is a \"real risk of significant harm\" to any individual whose personal information is involved. \"Significant harm\" includes bodily harm, humiliation, damage to reputation or relationships, loss of employment, business or professional opportunities, financial loss, identity theft, negative effects on the credit record, and damage to or loss of property."),
    PrivacyPolicyContentItem.heading("Notification to the Office of the Privacy Commissioner of Canada (OPC)", level = 2),
    PrivacyPolicyContentItem.paragraph("If We determine that there is a real risk of significant harm, We will report the breach to the OPC as soon as feasible. The report will include the prescribed information about the breach."),
    PrivacyPolicyContentItem.heading("Notification to Affected Individuals", level = 2),
    PrivacyPolicyContentItem.paragraph("If We determine that there is a real risk of significant harm to You, We will notify You as soon as feasible. The notification will be conspicuous and direct (e.g., via email or in-app message) and will include"),
    PrivacyPolicyContentItem.listItem("A description of the circumstances of the breach."),
    PrivacyPolicyContentItem.listItem("The date or approximate period of the breach."),
    PrivacyPolicyContentItem.listItem("A description of the personal information involved."),
    PrivacyPolicyContentItem.listItem("The steps We have taken to reduce the risk of harm."),
    PrivacyPolicyContentItem.listItem("The steps You can take to reduce Your risk of harm or mitigate any harm."),
    PrivacyPolicyContentItem.listItem("Contact information for further inquiry."),
    PrivacyPolicyContentItem.heading("Notification to Other Organizations", level = 2),
    PrivacyPolicyContentItem.paragraph("We will also notify any other organization or government institution that may be able to reduce or mitigate the risk of harm resulting from the breach (e.g., law enforcement), as appropriate."),
    PrivacyPolicyContentItem.heading("Record Keeping", level = 2),
    PrivacyPolicyContentItem.paragraph("We will keep a record of every breach of security safeguards involving personal information under Our control, regardless of whether it results in a real risk of significant harm. These records will be maintained for a minimum of two years."),
    PrivacyPolicyContentItem.heading("Changes to the Privacy Policy", level = 1, id = "policyChanges"),
    PrivacyPolicyContentItem.paragraph("We reserve the right to change this Privacy Policy from time to time. We will inform You of any significant changes by posting the updated notice in the App and on Our website. If We make any significant changes to Our notice, We will push a notification through the Sleep Tracker app and by e-mail."),
    PrivacyPolicyContentItem.heading("Contact Us", level = 1, id = "contact"),
    PrivacyPolicyContentItem.description("We encourage You to contact Us if You have any questions about the notice or about how We process Your personal information.  "),
    PrivacyPolicyContentItem.bullet("Email:", "privacysupport@sleeptracker.com"),
)

@Composable fun PrivacySleepPage() { PrivacyPageText("Sleep data privacy details") }
@Composable fun PrivacyJournalPage() { PrivacyPageText("Journal data privacy details") }
@Composable
fun PrivacyStatisticsPage(event: TransparencyEvent, navController: NavHostController) {
    // MIGRATION: The React Native statistics icon does not open a small
    //            tooltip; it swaps the whole tab body for
    //            PrivacyStatisticsPage.tsx. Compose renders the same privacy
    //            summary sections here so the top icon click has the same
    //            behavior as the source app.
    Column(Modifier.fillMaxWidth().padding(vertical = 20.dp)) {
        Text(getPrivacyRiskLabel(event.privacyRisk), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 12.dp))
        if (event.privacyRisk != PrivacyRisk.Low) {
            Text(formatPrivacyViolations(event), color = Color.White, fontSize = 14.sp, lineHeight = 20.sp, modifier = Modifier.padding(bottom = 18.dp))
        }
        PrivacyStatisticsSection("Purpose:", event.aiExplanation.why)
        if (event.privacyRisk == PrivacyRisk.Low) {
            PrivacyStatisticsSection("Storage:", event.aiExplanation.storage)
            PrivacyStatisticsSection("Access:", event.aiExplanation.access)
        }
        PrivacyActionLink("Privacy Policy Section") { navController.navigate(Routes.PrivacyPolicy) }
        PrivacyActionLink("PIPEDA Regulation") {
            // MIGRATION_FLAG: RN delegated this to Linking/openURL through
            //                 handleLinkPress. Common Compose has no platform
            //                 URL launcher wired yet, so this row keeps the
            //                 visible affordance without launching externally.
        }
        PrivacyActionLink("View Full Privacy Policy") { navController.navigate(Routes.PrivacyPolicy) }
    }
}
@Composable fun SensorNotAvailableWidget() { PrivacyPageText("This sensor is not available on this platform.") }

@Composable
fun PrivacyPageText(text: String) {
    Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AppColors.TooltipGreen).padding(20.dp)) { Text(text, color = Color.Black) }
}

@Composable
fun PrivacyStatisticsSection(label: String, value: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.LightBlack)
            .padding(15.dp),
    ) {
        Text(
            buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("$label ")
                pop()
                append(value)
            },
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        )
    }
}

@Composable
fun PrivacyActionLink(text: String, onClick: () -> Unit) {
    Text(
        text,
        color = AppColors.HyperlinkBlue,
        fontSize = if (text == "View Full Privacy Policy") 16.sp else 14.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .padding(top = if (text == "View Full Privacy Policy") 20.dp else 8.dp)
            .clickable(onClick = onClick),
    )
}

@Composable
fun PrivacyIcon(iconName: String, open: Boolean, onClick: () -> Unit) {
    val resource = when (iconName to open) {
        "privacy-high" to false -> Res.drawable.privacy_high
        "privacy-medium" to false -> Res.drawable.privacy_medium
        "privacy-low" to false -> Res.drawable.privacy_low
        "privacy-high" to true -> Res.drawable.privacy_high_open
        "privacy-medium" to true -> Res.drawable.privacy_medium_open
        else -> Res.drawable.privacy_low_open
    }
    // MIGRATION: RN's PrivacyIcon increases iconSize by 10 when open. KMP
    //            follows that status change so page-level privacy icons do not
    //            look static after being tapped.
    Image(painterResource(resource), null, Modifier.size(if (open) 60.dp else 50.dp).clickable(onClick = onClick))
}

@Composable
fun PrivacyTooltip(event: TransparencyEvent) {
    PrivacyTooltipHost(
        event = event,
        containerModifier = Modifier.size(40.dp),
    ) { modifier ->
        Image(
            painterResource(when (event.privacyRisk) { PrivacyRisk.High -> Res.drawable.privacy_high; PrivacyRisk.Medium -> Res.drawable.privacy_medium; PrivacyRisk.Low -> Res.drawable.privacy_low }),
            null,
            modifier.size(40.dp),
        )
    }
}

@Composable
fun PrivacyTooltipHost(event: TransparencyEvent, containerModifier: Modifier, anchor: @Composable (Modifier) -> Unit) {
    var visible by remember { mutableStateOf(false) }
    var pageX by remember { mutableStateOf(0f) }
    var pageY by remember { mutableStateOf(0f) }
    var iconSize by remember { mutableStateOf(IntSize.Zero) }
    var tooltipWidth by remember { mutableStateOf(0) }
    var tooltipHeight by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = windowInfo.containerSize.width.takeIf { it > 0 } ?: 1
    val screenHeight = windowInfo.containerSize.height.takeIf { it > 0 } ?: 1
    val spacingPx = with(density) { 8.dp.roundToPx() }
    val fallbackTooltipWidth = with(density) { 280.dp.roundToPx() }
    val fallbackTooltipHeight = with(density) { 320.dp.roundToPx() }
    val showAbove = pageY > screenHeight / 2f
    Box(containerModifier) {
        // MIGRATION: React Native measured pageY and showed the tooltip above
        //            or below the icon. Compose uses onGloballyPositioned to
        //            capture the same screen-relative position, then renders the
        //            details in a Popup so the open tooltip does not resize or
        //            wrap the header row that owns the icon.
        anchor(
            Modifier
            .onGloballyPositioned { coordinates ->
                val origin = coordinates.localToWindow(Offset.Zero)
                pageX = origin.x
                pageY = origin.y
                iconSize = coordinates.size
            }
            .clickable { visible = !visible },
        )
        if (visible) {
            val measuredTooltipWidth = tooltipWidth.takeIf { it > 0 } ?: fallbackTooltipWidth
            val maxTooltipLeft = (screenWidth - measuredTooltipWidth).coerceAtLeast(0)
            val desiredTooltipLeft = pageX.coerceIn(0f, maxTooltipLeft.toFloat())
            val xOffset = (desiredTooltipLeft - pageX).roundToInt()
            val yOffset = if (showAbove) {
                -(tooltipHeight.takeIf { it > 0 } ?: fallbackTooltipHeight) - spacingPx
            } else {
                iconSize.height + spacingPx
            }
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(x = xOffset, y = yOffset),
                properties = PopupProperties(focusable = false),
            ) {
                PrivacyTooltipCard(
                    event = event,
                    modifier = Modifier.onGloballyPositioned {
                        tooltipWidth = it.size.width
                        tooltipHeight = it.size.height
                    },
                    onClose = { visible = false },
                )
            }
        }
    }
}

@Composable
fun PrivacyTooltipCard(event: TransparencyEvent, modifier: Modifier = Modifier, onClose: () -> Unit) {
    Box(
        modifier
            .width(280.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(getPrivacyRiskColor(event.privacyRisk))
            .padding(16.dp),
    ) {
        Column {
            Text(getPrivacyRiskLabel(event.privacyRisk), color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Purpose:", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
            Text(event.aiExplanation.why, color = Color.Black, fontSize = 12.sp)
            Text("Storage:", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
            Text(event.aiExplanation.storage, color = Color.Black, fontSize = 12.sp)
            Text("Access:", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
            Text(event.aiExplanation.access, color = Color.Black, fontSize = 12.sp)
            Text("Close", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.End).clickable { onClose() }.padding(top = 8.dp))
        }
    }
}

@Composable
fun TimeDialog(title: String, defaultTime: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    val initial = remember(defaultTime) { parseTimeParts(defaultTime) }
    var hours by remember(defaultTime) { mutableStateOf(initial.hours) }
    var minutes by remember(defaultTime) { mutableStateOf(initial.minutes) }
    var ampm by remember(defaultTime) { mutableStateOf(initial.period) }
    var error by remember { mutableStateOf<String?>(null) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppColors.LightBlack,
        title = { Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    TimeInputBox("HH", hours) { value ->
                        val filtered = value.filter(Char::isDigit).take(2)
                        if (filtered.isEmpty() || (filtered.toIntOrNull() ?: 99) in 1..12) hours = filtered
                    }
                    Text(":", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp))
                    TimeInputBox("MM", minutes) { value ->
                        val filtered = value.filter(Char::isDigit).take(2)
                        if (filtered.isEmpty() || (filtered.toIntOrNull() ?: 99) in 0..59) minutes = filtered
                    }
                    Row(Modifier.padding(start = 10.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = .1f))) {
                        AmpmButton("AM", ampm == "AM") { ampm = "AM" }
                        AmpmButton("PM", ampm == "PM") { ampm = "PM" }
                    }
                }
                error?.let { Text(it, color = AppColors.TooltipRed, fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 12.dp)) }
            }
        },
        confirmButton = {
            TextButton({
                val parsedHours = hours.toIntOrNull()
                val parsedMinutes = minutes.toIntOrNull()
                if (parsedHours == null || parsedHours !in 1..12 || parsedMinutes == null || parsedMinutes !in 0..59) {
                    error = "Please enter valid hours (1-12) and minutes (0-59)."
                } else {
                    onSave("${parsedHours.toString().padStart(2, '0')}:${parsedMinutes.toString().padStart(2, '0')} $ampm")
                }
            }) { Text("Save", color = AppColors.GeneralBlue) }
        },
        dismissButton = { TextButton(onDismiss) { Text("Cancel", color = Color.White) } },
    )
}

@Composable
fun TimeInputBox(placeholder: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text(placeholder, color = Color(0xFF8E8E93), textAlign = TextAlign.Center) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.width(70.dp),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.White.copy(alpha = .1f), unfocusedContainerColor = Color.White.copy(alpha = .1f), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = Color.White, unfocusedTextColor = Color.White),
        shape = RoundedCornerShape(12.dp),
    )
}

@Composable
fun AmpmButton(label: String, selected: Boolean, onClick: () -> Unit) {
    Text(label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(if (selected) AppColors.GeneralBlue else Color.Transparent).clickable(onClick = onClick).padding(horizontal = 15.dp, vertical = 14.dp))
}

@Composable
fun JournalEntryDialog(initial: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var text by remember { mutableStateOf(initial) }
    AlertDialog(onDismissRequest = onDismiss, containerColor = AppColors.LightBlack, title = { Text("Diary", color = Color.White) }, text = { OutlinedTextField(text, { text = it }, modifier = Modifier.height(150.dp), colors = TextFieldDefaults.colors(focusedContainerColor = Color.White.copy(alpha = .1f), unfocusedContainerColor = Color.White.copy(alpha = .1f), focusedTextColor = Color.White, unfocusedTextColor = Color.White)) }, confirmButton = { TextButton({ onSave(text) }) { Text("Save") } }, dismissButton = { TextButton(onDismiss) { Text("Cancel") } })
}

@Composable
fun SleepNotesDialog(initial: List<SleepNote>, onDismiss: () -> Unit, onSave: (List<SleepNote>) -> Unit) {
    var selected by remember { mutableStateOf(initial.toSet()) }
    AlertDialog(onDismissRequest = onDismiss, containerColor = AppColors.LightBlack, title = { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Sleep notes", color = Color.White); Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.clickable(onClick = onDismiss)) } }, text = {
        Column { SleepNote.entries.forEach { note -> Text(note.label, color = Color.White, modifier = Modifier.padding(4.dp).clip(RoundedCornerShape(20.dp)).background(if (selected.contains(note)) AppColors.GeneralBlue else Color.White.copy(alpha = .1f)).clickable { selected = if (selected.contains(note)) selected - note else selected + note }.padding(horizontal = 15.dp, vertical = 8.dp)) } }
    }, confirmButton = { TextButton({ onSave(selected.toList()) }) { Text("Save") } }, dismissButton = { TextButton(onDismiss) { Text("Cancel") } })
}

fun getPrivacyRiskIcon(risk: PrivacyRisk): String = when (risk) { PrivacyRisk.High -> "privacy-high"; PrivacyRisk.Medium -> "privacy-medium"; PrivacyRisk.Low -> "privacy-low" }
fun getPrivacyRiskIconForPage(risks: List<PrivacyRisk>): String = when { risks.contains(PrivacyRisk.High) -> "privacy-high"; risks.contains(PrivacyRisk.Medium) -> "privacy-medium"; else -> "privacy-low" }
fun getPrivacyRiskColor(risk: PrivacyRisk): Color = when (risk) { PrivacyRisk.High -> AppColors.TooltipRed; PrivacyRisk.Medium -> AppColors.TooltipYellow; PrivacyRisk.Low -> AppColors.TooltipGreen }
fun getPrivacyRiskLabel(risk: PrivacyRisk): String = when (risk) { PrivacyRisk.High -> "Major Privacy Violation Detected:"; PrivacyRisk.Medium -> "Some Privacy Concerns Detected:"; PrivacyRisk.Low -> "No Privacy Violations Detected" }
fun formatPrivacyViolations(event: TransparencyEvent): String =
    if (event.regulatoryCompliance.issues.isBlank()) {
        "No privacy violations detected"
    } else {
        event.aiExplanation.privacyExplanation.ifBlank { event.regulatoryCompliance.issues }
    }

data class TimeParts(val hours: String, val minutes: String, val period: String)

fun parseTimeParts(defaultTime: String): TimeParts {
    val parts = Regex("""(\d{1,2}):(\d{2})\s*(AM|PM)?""", RegexOption.IGNORE_CASE).find(defaultTime)
    val rawHour = parts?.groupValues?.getOrNull(1)?.toIntOrNull()
    val rawMinutes = parts?.groupValues?.getOrNull(2)?.toIntOrNull()
    val rawPeriod = parts?.groupValues?.getOrNull(3)?.uppercase().orEmpty()
    if (rawHour == null || rawMinutes == null || rawMinutes !in 0..59) {
        return TimeParts("", "", "AM")
    }
    val period = rawPeriod.ifBlank { if (rawHour >= 12) "PM" else "AM" }
    val hour12 = when {
        rawHour == 0 -> 12
        rawHour > 12 -> rawHour - 12
        else -> rawHour
    }.coerceIn(1, 12)
    return TimeParts(hour12.toString().padStart(2, '0'), rawMinutes.toString().padStart(2, '0'), period)
}

fun newId(): String = "${Clock.System.now().toEpochMilliseconds()}-${Random.nextInt(100000)}"
fun nowIso(): String = Clock.System.now().toString()
fun todayInstant(): Instant = Clock.System.now()
fun todayIso(): String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
fun offsetDate(offset: Int): String = Clock.System.now().plus(offset.days).toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
fun parseIsoDateOrToday(value: String): LocalDate =
    runCatching { LocalDate.parse(value) }
        .getOrElse { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
fun weekDatesFor(selectedDate: String): List<String> {
    val selected = parseIsoDateOrToday(selectedDate)
    val sundayOffset = when (selected.dayOfWeek.name) {
        "SUNDAY" -> 0
        "MONDAY" -> 1
        "TUESDAY" -> 2
        "WEDNESDAY" -> 3
        "THURSDAY" -> 4
        "FRIDAY" -> 5
        else -> 6
    }
    val zone = TimeZone.currentSystemDefault()
    val startOfWeek = selected.atStartOfDayIn(zone).plus((-sundayOffset).days)
    // MIGRATION: RN Calendar derives the visible week from selectedDate and
    //            starts on Sunday. KMP uses the same seven date strings so
    //            tapping previous dates moves the highlight instead of keeping
    //            the calendar pinned to today.
    return (0..6).map { offset ->
        startOfWeek.plus(offset.days).toLocalDateTime(zone).date.toString()
    }
}
fun formatMonthDay(instant: Instant): String {
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    return "${months[dt.monthNumber - 1]} ${dt.dayOfMonth.toString().padStart(2, '0')}"
}
fun currentClockText(): String {
    val dt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = dt.hour % 12
    return "${(if (hour == 0) 12 else hour).toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}"
}
