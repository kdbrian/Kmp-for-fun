package com.kdbrian.weather.weatherapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import weatherapp.composeApp.BuildConfig

fun main() = application {

    Napier.base(DebugAntilog())

    Window(
        onCloseRequest = ::exitApplication,
        title = BuildConfig.release_version,
    ) {
        App()
    }
}