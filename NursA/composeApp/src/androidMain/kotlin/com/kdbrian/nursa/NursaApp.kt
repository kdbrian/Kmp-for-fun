package com.kdbrian.nursa

import android.app.Application
import com.kdbrian.nursa.di.appModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

class NursaApp : Application() {


    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        startKoin {

            modules(
                appModule()
            )
        }


    }


}