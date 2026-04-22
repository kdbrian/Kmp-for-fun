package com.farmconnect.admin.features.marketplace.usecases

import com.farmconnect.admin.core.domain.service.GenAiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import Napie.log.Napie

internal class GenerateProductTweakedNamesUseCase(
    private val aiService: GenAiService
) {

    operator fun invoke(
        productName: String,
        userSummary: String?,
    ): Flow<List<String>?> = flow {
        try {
           aiService
                .generateContentFromTextOnly(
                    """
                    You are an intelligent adviser for retail/wholesale farmers. You are supposed to assist 
                    a farmer trying to sell there products on this platform sell effectively. For now you are supposed
                    to tweak the product name represented by $productName the farmer might have some idea contained in $userSummary combine the
                    two to generate a series of names they can apply.
                    
                    STRICTLY
                    YOU ARE EXPECTED TO OUTPUT A LIST OF STRING MOSTLY ONE AND TWO WORDED FOR SIMPLICITY. NO OTHER TEXT OR MARKUP.
                    FOR EXAMPLE ["blazing product", "awesome product", ...]
                """.trimIndent()
                ).onSuccess {
                    Napie.tag("TweakedNamesUseCase").d("res : $it")
                    val strings = Json.decodeFromString<List<String>>(it)
                    emit(strings)
                }.onFailure {
                    Napie.tag("TweakedNamesUseCase").e("Failed  ${it.message}")
                }
        } catch (e: Exception) {
            Napie.tag("TweakedNamesUseCase").e(e)
            emit(emptyList())
        }
    }

}