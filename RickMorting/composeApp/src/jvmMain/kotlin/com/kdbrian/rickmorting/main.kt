package com.kdbrian.rickmorting

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kdbrian.rickmorting.di.appModules
import org.koin.core.context.startKoin

fun main() = application {
    startKoin { modules(appModules())  }


    Window(
        onCloseRequest = ::exitApplication,
        title = "RickMorting",
    ) {
        App()
    }
}