package com.kdbrian.rickmorting

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kdbrian.rickmorting.di.appModules
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    Napier.base(DebugAntilog())

    startKoin {
        modules(appModules())
    }


    ComposeViewport {
        App()
    }
}