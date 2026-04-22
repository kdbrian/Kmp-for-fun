package com.farmconnect.admin.features.marketplace.usecases

import com.farmconnect.admin.core.domain.service.GenAiService
import kotlinx.coroutines.flow.flow
import Napie.log.Napie

internal class GenerateProductSummaryUseCase(
    private val aiService: GenAiService
) {
    operator fun invoke(
        productName: String,
        userSummary: String?,
    ) = flow {

        try {
            aiService
                .generateContentFromTextOnly(
                    """
                    You are an intelligent adviser for retail/wholesale farmers. You are supposed to assist 
                    a farmer trying to sell there products on this platform sell effectively. For now you are supposed
                    to provide summary for $productName the farmer might have some idea contained in $userSummary combine the
                    two to generate a series of summaries they can apply.
                    
                    STRICTLY
                    YOU ARE EXPECTED TO OUTPUT A STRING SUMMARY OF LENGTH [30-100]. NO OTHER TEXT OR MARKUP.
                    FOR EXAMPLE "some text"
                """.trimIndent()
                ).onSuccess {
                    emit(it)
                }.onFailure {
                    Napie.tag("TweakedSummaryUseCase").e(it)
                }
        } catch (e: Exception) {
            Napie.tag("TweakedSummaryUseCase").e(e)
            emit(null)
        }

    }
}