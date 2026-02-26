# Kontrol
![Maven Central](https://img.shields.io/maven-central/v/io.github.chopyourbrain/kontrol)
![badge][badge-android]
![badge][badge-ios]

<img src="https://github.com/chopyourbrain/kontrol/blob/master/screenshots/1.png" width="281" height="600"><img src="https://github.com/chopyourbrain/kontrol/blob/master/screenshots/4.png" width="299" height="600">

<img src="https://github.com/chopyourbrain/kontrol/blob/master/screenshots/2.png" width="281" height="600"><img src="https://github.com/chopyourbrain/kontrol/blob/master/screenshots/3.png" width="299" height="600">

Kontrol - a Kotlin Multiplatform library for creating a debugging menu.

## Setup

### KMP:

Add a dependency to your build.gradle

```kotlin
val commonMain by getting {
    dependencies {
      implementation("io.github.chopyourbrain:kontrol:$kontrol_version")
    }
}

```

Android setup:

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        kontrolAndroidInstall(applicationContext)
    }
}
```

iOS setup (inside iosMain):

```kotlin
fun initIOS(navigationController: UINavigationController) {
    kontrolIOSInstall(navigationController)
}
```

Ktor network inspection setup:

```kotlin
val httpClient = HttpClient(okhttp) {
    install(KontrolKtorInterceptor) {
      databaseDriverFactory = DatabaseDriverFactory(applicationContext)
    }
}
```

### Android:

Add dependency to your build.gradle

```kotlin
implementation("io.github.chopyourbrain:kontrol-android:$kontrol_version")
```

Add setup to your application:

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        kontrolAndroidInstall(applicationContext)
    }
}
```

Okhttp inspection setup:

```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(KontrolOkhttpInterceptor(DatabaseDriverFactory(applicationContext)))
    .build()
```

## Using the Library

Inside common code

Create your properties by DSL: 

```kotlin
val properties = properties {
    group("Network") {
        switcher("Enable log", true) {
            //Check listener
        }
        dropDown("Server", listOf("google.com", "amazon.com", "reddit.com"), "google.com") {
            //Change listener
        }
        text("Default error", "Main Exception")
        button("Send request") {
            //Click listener
        }
    }
    group("App") {
        switcher("darkTheme", "Enable dark theme")
        dropDown("version", "Version", listOf("1.0", "2.0", "3.0"))
        text("Version code", "1.0")
        button("Kill application") {
            //Click listener
        }
    }
    button("Clear cache") {
        //Click listener
    }
}
```

Implement key-value storage for saving switcher and dropDown state (if nessesary):

```kotlin
val kvStorage = object : KVStorage {
    override fun getBoolean(key: String): Boolean? {
        return settings.getBooleanOrNull(key)
    }

    override fun getString(key: String): String? {
        return settings.getStringOrNull(key)
    }

    override fun setBoolean(key: String, value: Boolean) {
        settings[key] = value
    }

    override fun setString(key: String, value: String) {
        settings[key] = value
    }

}
```

### Important: 
If you want to use KVStorage, create switchers and dropDowns by this: 

```kotlin
switcher("KEY", "Description")
dropDown("KEY", "Description", listOf("First value", "Second value", "Third value"))
```

Then create DebugScreen object and use `show()` for open screen:

```kotlin
val debugScreen = createDebugScreen(properties, kvStorage)
debugScreen.show()
```

## No-Op for Release Builds

To avoid shipping debug UI code in release builds, swap the `kontrol` dependency for `kontrol-noop`. It has the same public API but all implementations are empty — no UI, no database, no network interception.

### KMP:

```kotlin
val commonMain by getting {
    dependencies {
        // debug builds
        debugImplementation("io.github.chopyourbrain:kontrol:$kontrol_version")
        // release builds — same API, zero overhead
        releaseImplementation("io.github.chopyourbrain:kontrol-noop:$kontrol_version")
    }
}
```

### Android:

```kotlin
debugImplementation("io.github.chopyourbrain:kontrol-android:$kontrol_version")
releaseImplementation("io.github.chopyourbrain:kontrol-noop:$kontrol_version")
```

No code changes required — all calls to `kontrolAndroidInstall`, `createDebugScreen`, `KontrolKtorInterceptor`, `KontrolOkhttpInterceptor`, etc. become no-ops at runtime.

## SAMPLE
[Click here](https://github.com/chopyourbrain/kontrol/tree/master/sample)

## ENJOY!

[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat
