package com.kdbrian.nursa.config.util

import io.github.aakira.napier.Napier

suspend inline fun <reified T> safeApiCall(
    block: suspend () -> T?
): Result<T> {
    return try {
        val result = block()
        if (result == null) {
            Result.failure(Exception("No data found."))
        } else {
            Result.success(result)
        }
    } catch (e: Exception) {
        Napier.d(e.message.toString())
        Result.failure(Exception("Something went wrong!"))
    }
}
