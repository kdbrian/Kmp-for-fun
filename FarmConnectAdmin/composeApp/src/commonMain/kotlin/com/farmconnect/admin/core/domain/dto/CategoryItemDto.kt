package com.farmconnect.admin.core.domain.dto

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class CategoryItemDto(
    val tags: List<String> = emptyList(),
    val categoryName: String = "",
    val categoryDescription: String = "",
    val id: String? = null,
    val imageUrl: String = "",
    val createOn: Long = Instant.now().epochSeconds,
    val updatedOn: Long = Instant.now().epochSeconds,
    @param:DrawableRes val imageId: Int? = null,
    val metadata: Map<String, String> = emptyMap()
) {


}

fun com.farmconnect.admin.core.domain.dto.CategoryItemDto.toEntity() =
    _root_ide_package_.com.farmconnect.admin.core.domain.model.CategoryItem(
        tags = tags,
        categoryName = categoryName,
        categoryDescription = categoryDescription,
        imageUrl = imageUrl,
        createOn = createOn,
        updatedOn = updatedOn,
        imagePath = imageId,
        categoryId = id ?: ""
    )

fun com.farmconnect.admin.core.domain.model.CategoryItem.toDto(): com.farmconnect.admin.core.domain.dto.CategoryItemDto {
    val dto = _root_ide_package_.com.farmconnect.admin.core.domain.dto.CategoryItemDto(
        tags = tags.toMutableList(),
        categoryName = categoryName,
        imageUrl = imageUrl,
        imageId = imagePath,
        id = categoryId,
        createOn = createOn,
        updatedOn = updatedOn,
        categoryDescription = categoryDescription,
        metadata = mapOf(
            "createdOn" to createOn.toString(),
        )
    )
    return dto
}

