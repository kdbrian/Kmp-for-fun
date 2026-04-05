This is a Kotlin Multiplatform project targeting Desktop (JVM). It consumes the Rick and Morty Api; perfect choice due to its support for GraphQl + Rest. Code is arranged, in a clean architecture that clearly separates concerns between the different layers of the App.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
 
* Targets : Desktop, Web(incomplete) & Android

## F* Around
- Networking - Ktor does it best
- Image loading - coil okhttp failure
- DI - Koin was a saviour
- Apollo GraphQl - used on Android but no fuss implementing for KMM

## Find out
- Even though coil would work fine on desktop and Jvm, it required a seperate network dependency for the web. Ended up using coil Ktor and Http engines since Okhttp is just for android
- Koin Dependency injection was seamless at common main but setting up the dependencies was initially tricky, used claude help.

# ✌ Cts
- For this project I focused on Kmp for desktop, Android and web. I have issues launching the web target but am steadily improving my Kmp knowledge.
- I utilized GraphQl since am OK with Rest. Pun intended

## Project Outline
### Targets
  - Android [X]
  - Web []
  - Desktop [X]
### Stack
  - Compose Multiplatform(Source+UI)
  - Kotlin
  - Clean Architecture
### Libraries
  - Coil - Image Loading
  - Ktor - Network
  - Apollo GraphQl - Api client
  - Koin - Dependency Injection

## I clap 👏 for me and urge to me to Keep the spirit alive

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
