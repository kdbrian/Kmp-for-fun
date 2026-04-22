package com.kdbrian.nursa

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kdbrian.nursa.di.appModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

fun main() = application {


    Napier.base(DebugAntilog())


    startKoin {
        modules(
            appModule()
        )
    }

    val windowState = rememberWindowState(
        placement = WindowPlacement.Floating,
        isMinimized = false
    )


    Window(
        onCloseRequest = ::exitApplication,
        title = "NursA",
        undecorated = true,
        state = windowState
    ) {
        App()
    }
}