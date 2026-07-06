# KMP Migration Audit

Source app: React Native 0.79.3 + Expo SDK 53 + TypeScript.
Target app: Kotlin Multiplatform + Compose Multiplatform 1.8.x.

## Official Setup References

- Compose Multiplatform common ViewModel is supported through `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose`; the Kotlin docs describe common-code ViewModels backed by `StateFlow`.
- Compose Navigation is available through `org.jetbrains.androidx.navigation:navigation-compose`.
- SQLDelight 2.x generates type-safe Kotlin APIs from existing SQL schemas, so it is used here to preserve the `journals` and `sensor_data` table contracts.
- KMP `expect` / `actual` declarations are used for Android Keystore, iOS Keychain, platform crypto, platform sensors, audio, and background services.

## Route Mapping

| React Native route | Kotlin destination/composable |
| --- | --- |
| `app/(auth)/index.tsx` | `AuthScreen(mode = Login)` |
| `app/(auth)/register.tsx` | `AuthScreen(mode = Register)` |
| `app/(onboarding)/index.tsx` | `OnboardingScreen(step = Microphone)` |
| `app/(onboarding)/accelerometer-consent.tsx` | `OnboardingScreen(step = Accelerometer)` |
| `app/(onboarding)/light-sensor-consent.tsx` | `OnboardingScreen(step = LightSensor)` |
| `app/(onboarding)/journal-data.tsx` | `OnboardingScreen(step = JournalData)` |
| `app/(onboarding)/cloud-storage.tsx` | `OnboardingScreen(step = CloudStorage)` |
| `app/(onboarding)/privacy-policy-agreement.tsx` | `OnboardingScreen(step = PrivacyAgreement)` |
| `app/(onboarding)/transparency.tsx` | `OnboardingScreen(step = Transparency)` |
| `app/(onboarding)/questions-explanation.tsx` | `OnboardingScreen(step = QuestionsIntro)` |
| `app/(onboarding)/questions.tsx` | `OnboardingScreen(step = Questions)` |
| `app/(tabs)/sleep/index.tsx` | `SleepScreen` |
| `app/(tabs)/sleep/sleep-mode.tsx` | `SleepModeScreen` |
| `app/(tabs)/journal.tsx` | `JournalScreen` |
| `app/(tabs)/statistics.tsx` | `StatisticsScreen` |
| `app/(tabs)/profile/index.tsx` | `ProfileScreen` |
| `app/(tabs)/profile/consent-preferences.tsx` | `ConsentPreferencesScreen` |
| `app/privacy-policy.tsx` | `PrivacyPolicyScreen` |

## Component Mapping

| React Native component | Kotlin composable |
| --- | --- |
| `AuthInput` | `AuthInputField` |
| `GeneralButton` | `GeneralButton` |
| `OnboardingHeader` | `OnboardingHeader` |
| `PermissionsToggle` | `PermissionsToggle` |
| `OnboardingQuestionOption` | `OnboardingQuestionOption` |
| `NormalSleepPage` | `NormalSleepPage` |
| `NormalJournalPage` | `NormalJournalPage` |
| `DailyStatisticsPage` | `DailyStatisticsPage` |
| `StatisticItem` | `StatisticItem` |
| `Calendar` | `CalendarStrip` |
| `TimeModal` | `TimeDialog` |
| `JournalEntryModal` | `JournalEntryDialog` |
| `SleepNotesModal` | `SleepNotesDialog` |
| `MenuItem` | `ProfileMenuItem` |
| `PrivacyIcon` | `PrivacyIcon` |
| `SensorPrivacyIcon` | `SensorPrivacyIcon` |
| `PrivacyTooltip` | `PrivacyTooltip` |
| `PrivacySleepPage` | `PrivacySleepPage` |
| `PrivacyJournalPage` | `PrivacyJournalPage` |
| `PrivacyStatisticsPage` | `PrivacyStatisticsPage` |

## Type Mapping

| TypeScript type | Kotlin equivalent |
| --- | --- |
| `User` | `data class User` |
| `UserConsentPreferences` | `data class UserConsentPreferences` |
| `JournalData` | `data class JournalData` |
| `SleepNote` union | `enum class SleepNote` |
| `GeneralSleepData` | `data class GeneralSleepData` |
| `SensorData` union | `sealed interface SensorData` with typed data classes |
| `TransparencyEvent` | `data class TransparencyEvent` |
| TypeScript enums | Kotlin `enum class` with `wireName` |
| Zustand stores | ViewModel + Repository + `StateFlow` |
| React component loading/error flags | `sealed interface UiState<out T>` |

## Expo / React Native API Mapping

| Expo / RN API | Kotlin Multiplatform equivalent |
| --- | --- |
| `expo-router` | Navigation Compose `NavHost`; bottom tabs with `NavigationBar` |
| `expo-font` | Compose resources font `space_mono_regular.ttf` |
| `expo-secure-store` | `expect/actual SecureKeyValueStore`; Android SharedPreferences/Keystore-compatible boundary, iOS Keychain stub |
| `expo-sqlite` | SQLDelight 2.x schema-preserving database |
| `expo-crypto` / `crypto-js` | `expect/actual CryptoProvider`; Android `javax.crypto`, iOS CryptoKit stub |
| `expo-sensors` | `expect/actual SensorGateway`; Android `SensorManager`, iOS light sensor unavailable |
| `expo-av` | `expect/actual AudioGateway`; Android `AudioRecord`, iOS AVFoundation stub |
| `expo-task-manager` / `expo-background-fetch` | Android ForegroundService + WorkManager, iOS BackgroundTasks stub |
| `ImageBackground` / `Image` | Compose resources + `Image`/`Box` |
| `TouchableOpacity` | Compose `Modifier.clickable` / `Button` |
| `Modal` | Compose `Dialog` |
| `AsyncStorage` | `KeyValueStore` backed by platform preferences |

## Async Pattern Mapping

- `useEffect` initialization becomes `ViewModel.init { viewModelScope.launch { ... } }`.
- `async/await` repository calls become `suspend` functions.
- `setTimeout` loading delays become `delay`.
- Zustand setters become atomic `MutableStateFlow.update`.
- Promise error handling becomes `runCatching` or `try/catch` with typed `UiState.Error`.

## Screens Requiring Strict Visual Parity

- Login/register: black background, exact titles/subtitles, auth fields, eye icon, button, and register/sign-in row.
- Onboarding: original multi-step image/text/toggle flow, with scrollable bottom panels to avoid small-device overflow.
- Sleep: `Sleep Tracker` header, sleep wheel image, bedtime/alarm cards, `SLEEP NOW`.
- Sleep mode: background image, current time, alarm, hold-to-wake behavior, transparency controls.
- Journal: image header, calendar, sleep goal card, diary card, sleep notes card, activity tracker.
- Statistics: daily tabs, calendar header, sleep quality card, sleep stages image, stage grid, insights grid, sleep clips waveform rows.
- Profile: centered header, `Hello, firstName`, menu rows, logout button.
- Consent preferences: original toggle order and link copy.

## Known Platform Flags

- `// MIGRATION_FLAG`: iOS light sensor is not consistently available through public APIs; the target uses `SensorNotAvailableWidget`.
- `// MIGRATION_FLAG`: Android ForegroundService requires notification permission on Android 13+ and a persistent notification on Android 8+.
- `// MIGRATION_FLAG`: Cloud endpoints may return HTML fallback pages; repositories must fall back to local storage rather than surfacing raw `<!DOCTYPE html>` errors.
- `// MIGRATION_FLAG`: iOS secure storage/background/audio actual implementations are stubs in this Windows-authored project and must be completed on macOS/Xcode.
