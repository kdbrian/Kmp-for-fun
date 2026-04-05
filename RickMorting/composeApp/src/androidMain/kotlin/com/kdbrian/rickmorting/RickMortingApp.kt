package com.kdbrian.rickmorting

import android.app.Application
import com.kdbrian.rickmorting.di.appModules
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

class RickMortingApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        startKoin {
            modules(appModules())
        }
    }


}