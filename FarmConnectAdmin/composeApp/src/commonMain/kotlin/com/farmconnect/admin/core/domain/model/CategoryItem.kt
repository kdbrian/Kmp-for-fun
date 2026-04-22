package com.farmconnect.admin.core.domain.model

import androidx.annotation.DrawableRes
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
open class CategoryItem(
    open val categoryId: String = "",
    open val tags: List<String> = emptyList(),
    open val categoryName: String = "",
    open val categoryDescription: String = "",
    open val status: String = "",
    open val imageUrl: String = "",
    @param:DrawableRes open val imagePath: Int? = null,
    open val createOn: Long = Instant.now().epochSeconds,
    open val updatedOn: Long = Instant.now().epochSeconds,
) {
    internal constructor() : this(
        categoryId = "",
        tags = emptyList(),
        categoryName = "",
        categoryDescription = "",
        status = "",
        imageUrl = "",
        imagePath = null,
        createOn = Instant.now().epochSeconds,
        updatedOn = Instant.now().epochSeconds,
    )


    // --- Builder Class ---

    /**
     * Builder class for creating CategoryItem instances.
     */
    class Builder {
        private var categoryId: String = ""
        private var tags: List<String> = emptyList()
        private var categoryName: String = ""
        private var categoryDescription: String = ""
        private var status: String = ""
        private var imageUrl: String = ""

        @DrawableRes
        private var imagePath: Int? = null
        private var createOn: Long = Instant.now().epochSeconds
        private var updatedOn: Long = Instant.now().epochSeconds

        /**
         * Primary constructor for the Builder.
         */
        constructor()

        /**
         * Constructor to initialize the Builder from a CategoryItemDto.
         */
        constructor(dto: com.farmconnect.admin.core.domain.dto.CategoryItemDto) {
            this.categoryId = dto.id ?: "" // Use id from DTO, default to empty string
            this.tags = dto.tags
            this.categoryName = dto.categoryName
            this.categoryDescription = dto.categoryDescription
            this.imageUrl = dto.imageUrl
            this.imagePath = dto.imageId // Use imageId from DTO
            this.createOn = dto.createOn
            this.updatedOn = dto.updatedOn
            // Status isn't in the DTO, so it retains its default value ""
        }

        // Setter methods (chainable)
        fun categoryId(categoryId: String) = apply { this.categoryId = categoryId }
        fun tags(tags: List<String>) = apply { this.tags = tags }
        fun categoryName(categoryName: String) = apply { this.categoryName = categoryName }
        fun categoryDescription(categoryDescription: String) =
            apply { this.categoryDescription = categoryDescription }

        fun status(status: String) = apply { this.status = status }
        fun imageUrl(imageUrl: String) = apply { this.imageUrl = imageUrl }
        fun imagePath(@DrawableRes imagePath: Int?) = apply { this.imagePath = imagePath }
        fun createOn(createOn: Long) = apply { this.createOn = createOn }
        fun updatedOn(updatedOn: Long) = apply { this.updatedOn = updatedOn }

        /**
         * Builds and returns the final CategoryItem instance.
         */
        fun build(): com.farmconnect.admin.core.domain.model.CategoryItem {
            return _root_ide_package_.com.farmconnect.admin.core.domain.model.CategoryItem(
                categoryId = categoryId,
                tags = tags,
                categoryName = categoryName,
                categoryDescription = categoryDescription,
                status = status,
                imageUrl = imageUrl,
                imagePath = imagePath,
                createOn = createOn,
                updatedOn = updatedOn
            )
        }
    }

}

