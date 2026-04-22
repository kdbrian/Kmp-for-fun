package com.farmconnect.admin.core.domain.model

import com.farmconnect.admin.core.util.SavedPlace
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ProductOrServiceItem(
    val id: String? = null,
    val stock: Double? = null,
    val errors : List<String>? = null,
    val tags: List<String> = emptyList(),
    val title: String? = null,
    val templateId: String? = null,
    val images: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val description: String? = null,
    val price: Double? = null,
    val discount: Double? = null,
    val rating: Float? = null,
    val availabilityZones : List<SavedPlace> = emptyList(),
    val createdOn: Long = Instant.now().epochSeconds,
    val updatedOn: Long = Instant.now().epochSeconds,
    val metadata: Map<String, String> = emptyMap()
)


fun com.farmconnect.admin.core.domain.model.ProductOrServiceItem.toDto() =
    _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto(
        id = id,
        metadata = metadata.toMutableMap().apply {
            _root_ide_package_.kotlin.collections.MutableMap.put("updatedOn", updatedOn.toString())
            _root_ide_package_.kotlin.collections.MutableMap.put("createdOn", createdOn.toString())
        },
        templateId = templateId,
        tags = tags,
        categories = categories,
        description = description,
        title = title,
        stock = stock,
        errors = errors,
        rating = rating,
        price = price,
        images = images,
        isDiscounted = discount != null,
        discount = discount,
        locations = availabilityZones
    )