package com.farmconnect.admin.core.domain.dto

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.random.Random
import kotlin.time.Instant

@Serializable
data class ProductTemplateDto(
    override val templateId: String = UUID.randomUUID().toString().split("-").first(),
    override val productName: String = "",
    override val productDescription: String = "",
    override val productAveragePrice: Double = 0.0,
    override val addedBy: String = "",
    val isFavourite: Boolean = false,
    override val usedBy: List<String> = emptyList(),
    override val categories: List<String>,
    override val reviews: List<String>,
    override val addedOn: Long = Instant.now().epochSeconds,
    override val updatedOn: Long = Instant.now().epochSeconds
) : com.farmconnect.admin.core.domain.model.ProductTemplate {
    companion object {
        val demoTemplate = _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductTemplateDto(
            productName = "Demo Product",
            productDescription = "Demo Product Description",
            productAveragePrice = Random.nextDouble(99.99, 999.99),
            addedBy = "Demo User",
            addedOn = Instant.now().epochSeconds.minus(1000 * 24 * 60),
            updatedOn = Instant.now().epochSeconds,
            categories = listOf("Demo Category"),
            reviews = listOf("Demo Review"),
            isFavourite = false,
            usedBy = listOf("Demo User")
        )
    }
}


