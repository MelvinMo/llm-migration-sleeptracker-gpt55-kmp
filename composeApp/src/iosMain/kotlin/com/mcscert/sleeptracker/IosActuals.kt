package com.mcscert.sleeptracker

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.mcscert.sleeptracker.db.SleepTrackerDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(SleepTrackerDatabase.Schema, "sleeptracker_data.db")
}

actual class SecureKeyValueStore {
    actual suspend fun read(key: String): String? = null
    actual suspend fun write(key: String, value: String) {
        // MIGRATION_FLAG: Replace with Keychain Services actual on macOS/Xcode.
    }
    actual suspend fun delete(key: String) {
        // MIGRATION_FLAG: Replace with Keychain Services actual on macOS/Xcode.
    }
}

actual class CryptoProvider actual constructor(private val store: SecureKeyValueStore) {
    actual suspend fun encrypt(plainText: String): String = plainText
    actual suspend fun decrypt(cipherText: String): String = cipherText
}

actual class SensorGateway {
    actual suspend fun isLightAvailable(): Boolean = false
    actual suspend fun isAccelerometerAvailable(): Boolean = true
    actual suspend fun isAudioAvailable(): Boolean = false
    actual suspend fun startAccelerometer(onSample: (AccelerometerSensorData) -> Unit) {
        // MIGRATION_FLAG: Use CoreMotion actual implementation on macOS/Xcode.
    }
    actual suspend fun stopAccelerometer() {
        // MIGRATION_FLAG: Use CoreMotion actual implementation on macOS/Xcode.
    }
}

actual class AudioGateway {
    actual suspend fun requestPermission(): Boolean = false
}

actual class BackgroundTaskGateway {
    actual suspend fun updateAccelerometerEnabled(enabled: Boolean) {
        // MIGRATION_FLAG: Map to BackgroundTasks when iOS target is completed.
    }
}
