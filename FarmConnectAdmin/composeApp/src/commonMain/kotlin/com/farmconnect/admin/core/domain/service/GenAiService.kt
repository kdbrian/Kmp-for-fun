package com.farmconnect.admin.core.domain.service

import android.graphics.Bitmap

interface GenAiService {

    suspend fun generateContentFromTextOnly(prompt: String): Result<String>

    suspend fun generateContentFromTextAndImage(image: Bitmap, prompt: String): Result<String>

}