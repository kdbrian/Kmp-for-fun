package com.farmconnect.admin.core.data.impl

import android.graphics.Bitmap
import com.farmconnect.admin.core.domain.service.GenAiService

import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseGenAiService : GenAiService {

    private val ai = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    )
    private val model = ai.generativeModel(
        modelName = "gemini-2.5-pro",
    )

    override suspend fun generateContentFromTextOnly(prompt: String): Result<String> = withContext(
        Dispatchers.IO
    ) {
        try {
            Result.success(model.generateContent(prompt).text ?: "Something unexpected occurred.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateContentFromTextAndImage(
        image: Bitmap,
        prompt: String
    ): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val contentResponse = model.generateContent(
                    prompt = content {
                        image(image)
                        text(prompt)
                    }
                )

                Result.success(contentResponse.text.toString())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}