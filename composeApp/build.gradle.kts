import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    // MIGRATION: iOS targets are declared so expect/actual boundaries match the
    //            requested KMP architecture. Android is the verifiable target in
    //            this Windows workspace; iOS actuals are stubs where hardware or
    //            Keychain/CryptoKit require Xcode verification.
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
                implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.2")
                implementation("app.cash.sqldelight:runtime:2.3.2")
                implementation("app.cash.sqldelight:coroutines-extensions:2.3.2")
                implementation("io.ktor:ktor-client-core:3.1.3")
                implementation("io.ktor:ktor-client-content-negotiation:3.1.3")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.3")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.10.1")
                implementation("app.cash.sqldelight:android-driver:2.3.2")
                implementation("io.ktor:ktor-client-okhttp:3.1.3")
                implementation("androidx.work:work-runtime-ktx:2.10.1")
                implementation("androidx.core:core-ktx:1.16.0")
                implementation("androidx.security:security-crypto:1.1.0-alpha06")
            }
        }
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("app.cash.sqldelight:native-driver:2.3.2")
                implementation("io.ktor:ktor-client-darwin:3.1.3")
            }
        }
    }
}

android {
    namespace = "com.mcscert.sleeptracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mcscert.sleeptracker.kmpdev"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        val apiUnencryptedUrl = providers.gradleProperty("apiUnencryptedUrl")
            .orElse("http://YOUR_LAN_IP:7000/api")
            .get()
        val apiEncryptedUrl = providers.gradleProperty("apiEncryptedUrl")
            .orElse(apiUnencryptedUrl)
            .get()
        buildConfigField("String", "API_UNENCRYPTED_URL", "\"$apiUnencryptedUrl\"")
        buildConfigField("String", "API_ENCRYPTED_URL", "\"$apiEncryptedUrl\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.resources {
    packageOfResClass = "com.mcscert.sleeptracker.resources"
}

sqldelight {
    databases {
        create("SleepTrackerDatabase") {
            packageName.set("com.mcscert.sleeptracker.db")
        }
    }
}
