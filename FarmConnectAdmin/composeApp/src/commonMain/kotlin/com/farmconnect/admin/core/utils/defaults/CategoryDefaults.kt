package com.farmconnect.admin.core.utils.defaults

import kotlin.time.Instant

object CategoryDefaults {

    // 1. Standard, fully-defined category
    val ELECTRONICS_CATEGORY = _root_ide_package_.com.farmconnect.admin.core.domain.dto.CategoryItemDto(
        id = "cat_101",
        categoryName = "Electronics",
        categoryDescription = "Gadgets, computing, and high-tech devices.",
        tags = listOf("Tech", "Digital", "Hardware"),
        imageUrl = "https://example.com/images/cat_electronics.jpg",
        createOn = 1733596800000L, // Dec 8, 2024
        updatedOn = Instant.now().epochSeconds,
        metadata = mapOf("department_code" to "ELEC", "popular_sort" to "desc")
    )

    // 2. Apparel category with minimal data (e.g., no imageId, no metadata)
    val APPAREL_CATEGORY = _root_ide_package_.com.farmconnect.admin.core.domain.dto.CategoryItemDto(
        id = "cat_202",
        categoryName = "Apparel & Clothing",
        categoryDescription = "Shop the latest trends in fashion for all ages.",
        tags = listOf("Fashion", "Style", "Seasonal"),
        imageUrl = "https://example.com/images/cat_apparel.jpg",
        createOn = 1733596800000L,
        updatedOn = Instant.now().epochSeconds,
        imageId = null, // Null optional image ID
        metadata = emptyMap() // Empty metadata
    )

    // 3. Category for services (less reliance on image/drawable, specific tags)
    val CONSULTING_CATEGORY = _root_ide_package_.com.farmconnect.admin.core.domain.dto.CategoryItemDto(
        id = "cat_303",
        categoryName = "Business Consulting",
        categoryDescription = "Professional services for business development and strategy.",
        tags = listOf("B2B", "Service", "Expert"),
        imageUrl = "https://example.com/images/cat_consulting.png",
        createOn = 1733596800000L,
        updatedOn = Instant.now().epochSeconds,
        metadata = mapOf("service_type" to "hourly", "region" to "global")
    )

    // 4. Default/empty category (useful for initial state or loading tests)
    val DEFAULT_CATEGORY = _root_ide_package_.com.farmconnect.admin.core.domain.dto.CategoryItemDto(
        id = null, // Null ID for a non-persisted category
        categoryName = "Unassigned",
        categoryDescription = "Items that have not been categorized yet.",
        tags = emptyList(),
        imageUrl = "", // Empty required string
        createOn = Instant.now().epochSeconds,
        updatedOn = Instant.now().epochSeconds,
        imageId = null,
        metadata = emptyMap()
    )

    val ALL_CATEGORIES = listOf(
        ELECTRONICS_CATEGORY,
        APPAREL_CATEGORY,
        CONSULTING_CATEGORY,
        DEFAULT_CATEGORY
    )
}