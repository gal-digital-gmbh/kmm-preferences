![Maven Central](https://img.shields.io/maven-central/v/de.gal-digital/kmm-preferences?style=flat-square)
![badge][badge-android]
![badge][badge-ios]

# SharedPreference in Kotlin Multiplatform Mobile (KMM)

This is a Kotlin MultiPlatform library that provides access to the shared preferences on iOS & Android.

## setup

```kotlin
kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("de.gal-digital:kmm-preferences:x.x.x")
      }
    }
  }
}
```

## usage

```kotlin
class Settings(sharedPreferences : SharedPreferences) {

  private val jsonSerializer = Json {}

  var token : String? by preference(sharedPreferences, "token", null)

  var user : SomeSerializable? by serializable(sharedPreferences,jsonSerializer,"user", null)
}
```

[badge-android]: http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat
[badge-ios]: http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat