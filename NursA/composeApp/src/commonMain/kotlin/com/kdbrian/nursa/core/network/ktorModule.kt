package com.kdbrian.nursa.core.network

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

fun ktorModule() = module {
    single {
        HttpClient(CIO) {

            install(ContentNegotiation) {
                json(
                    json = kotlinx.serialization.json.Json {
//                        prettyPrint = true
//                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Napier.v(message = message)
                    }
                }
            }
        }
    }
}