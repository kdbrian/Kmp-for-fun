package com.farmconnect.core.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.NumberFormat
import java.util.Locale

fun Context.showToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Double.toCurrency(locale: Locale = Locale.getDefault()): String {
    val format = NumberFormat.getCurrencyInstance(locale).format(this)
    val replace = format.replace("[$€£]".toRegex(), "")
    return "Ksh. ".plus(replace)
}

fun Context.uriToBitmap(imageUri: Uri): Bitmap? {
    return MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
}

private fun <T> Flow<T>.pairwise(): Flow<Pair<T, T>> = flow {
    var previous: T? = null
    collect { value ->
        if (previous != null) emit(previous!! to value)
        previous = value
    }
}
