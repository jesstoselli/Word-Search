# Word Search

### Mobile Challenge 🏅 2022 - Dictionary

  
> This is a challenge by [Coodesh](https://coodesh.com/).


Word Search is a Kotlin Android application built minding the Coodesh technical challenge, in order to demonstrate core Android
Development skills.

It makes use of the [Free Dictionary Api](https://dictionaryapi.dev/).

It's important to mention it takes a bit to load when first installing, but runs smoothly afterwards.

This app demonstrates the following views and techniques using libs:

* [Retrofit](https://square.github.io/retrofit/) for making API calls to an HTTP web service.
* [Ok HTTP](https://square.github.io/okhttp/) for handing network calls.
* [Moshi](https://github.com/square/moshi) which handles the deserialization of the returned JSON to Kotlin data objects.
* [Gson](https://github.com/google/gson) for converting data between app and database.
* [Room](https://developer.android.com/training/data-storage/room) for local database storage.
* [Koin](https://insert-koin.io/docs/reference/koin-android/start) for dependency injection.
* [JUnit4](https://junit.org/junit4/) for unit tests.
* [Espresso](https://developer.android.com/training/testing/espresso) for UI tests.
* [Mockk](https://mockk.io/ANDROID.html) for mocking objetcs in unit testing.

It leverages the following components from the Jetpack library:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) with binding adapters
* [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) with the SafeArgs plugin for parameter
  passing between fragments
* [Material Design](https://m3.material.io/)

## Getting Started

To get started with this project, simply pull the repository and import the project into Android Studio. From there, deploy the
project to an emulator or device.

## Report Issues

Notice any issues with a repository? Please file a github issue in the repository.
