package com.farmconnect.admin

import FarmConnectAdmin.composeApp.BuildConfig
import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.farmconnect.data.dataModule
import com.farmconnect.di.authModule
import com.farmconnect.di.businessModule
import com.farmconnect.di.clientModule
import com.farmconnect.di.coreModule
import com.farmconnect.di.useCaseModule
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FarmConnectApp : Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Napier.base(DebugAntilog())

//        FirebaseApp.initializeApp(applicationContext.applicationContext)
//
//        Places.initializeWithNewPlacesApiEnabled(applicationContext.applicationContext,BuildConfig.placesApiKey)

        startKoin {

            modules(
                authModule,
                businessModule,
                clientModule,
                coreModule(),
                dataModule,
                useCaseModule,
            )
        }

    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)

            .build()
    }


}