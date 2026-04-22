package com.farmconnect.admin.core.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> safeApiCall(crossinline call: suspend () -> T?): Result<T> {
    return withContext(Dispatchers.Default) {
        try {
            val t = call()
            if (t == null)
                Result.failure<T>(Exception("null response"))
            else
                Result.success(t)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}