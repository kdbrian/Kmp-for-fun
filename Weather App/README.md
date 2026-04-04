This is a Kotlin Multiplatform project targeting Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.

## F* Around
- Logging - Used to timber on android does't work on Kmp.
- Networking - Ktor does it best
- UI - Jetpack compose + Compose Multiplatform
- Resources - used to R. extension on android
- Image loading - coil still works fine
- IDE - Tried intellij first. Tooling, live templates, plus other dev perks to speed up work not supported.

## Find out
- [Napier](https://github.com/aakira/napier) works fine for Kmp logging.
- Android Studio - Heavy but does the work fine, especially previews and templates.
- [gradle-buildconfig-plugin]("https://github.com/gmazzo/gradle-buildconfig-plugin") Replacement for secrets, IDK of any tricks but everytime I rerun the project I ended up( clean, generate BuildConfig class, run) Tideous but saves my secrets from exposure.
- Hot ♨ reload - learnt about it later, still heavy and not fast as in Flutter.
- Importing resources is easier just adding them to the resources dir in commonMain.

# ✌ Cts
- For this project I focused on Kmp for desktop apps but future projects am considering a Android/ Web alternative.
- I can't say I can't build for Ios due to lack of a Mac. But at this pace, am assured I don't need one to deliver Ios apps.
- Still getting to know Kmp.

## I clap 👏 for me and urge to me to Keep the spirit alive

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
