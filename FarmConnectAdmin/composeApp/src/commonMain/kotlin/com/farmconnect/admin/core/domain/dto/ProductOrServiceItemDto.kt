package com.farmconnect.admin.core.domain.dto

import com.farmconnect.admin.core.util.SavedPlace
import com.farmconnect.admin.core.util.UnitType
import kotlinx.serialization.Serializable

@Serializable
data class ProductOrServiceItemDto(
    val id: String? = null,
    val templateId: String? = null,
    val tags: List<String> = emptyList(),
    val errors: List<String>? = null,
    val images: List<String> = emptyList(),
    val title: String? = null,
    val rating: Float? = null,
    val unitType: UnitType = UnitType.default,
    val ratingCount: Long? = null,
    val price: Double? = null,
    val categories: List<String> = emptyList(),
    val description: String? = null,
    val stock: Double? = null,
    val metadata: Map<String, String> = emptyMap(),
    val isDiscounted: Boolean? = null,
    val discount: Double? = null,
    val locations: List<SavedPlace> = emptyList(),
) {
    companion object {
        val demoProductDto =
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto(
                price = 234.99,
                discount = 10.0,
                rating = 3.15f,
                metadata = mapOf(
                    "discount" to "30%", "delivery" to "free"
                ),
                title = "Demo Product",
                description = "Some description",
                images = listOf("https://picsum.photos/200/300"),
                categories = listOf("Category 1", "Category 2"),
                tags = listOf("Tag 1", "Tag 2"),
            )
    }
}

fun com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto.toEntity() =
    _root_ide_package_.com.farmconnect.admin.core.domain.model.ProductOrServiceItem(
        title = title.toString(),
        discount = discount,
        images = images,
        categories = categories,
        metadata = metadata,
        templateId = templateId,
        id = id,
        stock = stock,
        tags = tags,
        price = price,
        rating = rating,
        description = description,
        availabilityZones = locations
    )
