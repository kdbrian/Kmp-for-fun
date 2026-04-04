package com.kdbrian.weather.weatherapp.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorProvider {

    val client = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
//                isLenient = true
                ignoreUnknownKeys=true
            })

        }

    }


}