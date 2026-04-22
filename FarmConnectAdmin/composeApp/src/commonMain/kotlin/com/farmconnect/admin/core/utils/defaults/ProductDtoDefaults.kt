package com.farmconnect.admin.core.utils.defaults

object ProductDtoDefaults {

    // 1. A complete, high-rated, in-stock product
    val demoProduct = _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto(
        id = "prod_001",
        templateId = "product_standard",
        tags = listOf("Electronics", "New Arrival", "Best Seller"),
        images = listOf("https://example.com/images/4k_monitor.jpg",),
        title = "4K Ultra HD Monitor 32-inch",
        rating = 4.7f,
        price = 499.99,
        categories = listOf("Electronics", "Displays"),
        description = "Stunning 32-inch 4K UHD monitor with HDR support and low blue light mode.",
        stock = 15.0,
        metadata = mapOf("screen_size" to "32", "resolution" to "3840x2160"),
        isDiscounted = false,
        discount = null
    )

    // 2. A discounted service (stock is null for service/unlimited)
    val demoServiceDiscounted =
        _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto(
            id = "svc_005",
            templateId = "service_hourly",
            tags = listOf("Consulting", "Remote", "Special Offer"),
            images = listOf("https://example.com/images/consulting_service.png",),
            title = "Premium 1-Hour Remote Tech Consultation",
            rating = 4.9f,
            price = 150.00,
            categories = listOf("Services", "IT Support"),
            description = "Book a one-hour session with a Senior Tech Architect.",
            stock = null, // Null stock for a service
            metadata = mapOf("duration_minutes" to "60", "availability" to "weekdays"),
            isDiscounted = true,
            discount = 25.00 // $25 off
        )

    // 3. An item that is out of stock
    val demoProductOutOfStock =
        _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto(
            id = "prod_010",
            templateId = "product_clothing",
            tags = listOf("Apparel", "Summer", "Clearance"),
            images = listOf("https://example.com/images/linen_shirt.jpg",),
            title = "Men's Classic Linen Shirt (Blue)",
            rating = 4.2f,
            price = 59.99,
            categories = listOf("Fashion", "Shirts"),
            description = "Lightweight and breathable linen shirt for hot weather.",
            stock = 0.0,
            metadata = mapOf("color" to "Blue", "material" to "Linen"),
            isDiscounted = false,
            discount = null
        )

    // 4. A new item with no rating yet and minimal metadata
    val demoProductNew = _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto(
        id = "prod_015",
        templateId = "product_book",
        tags = listOf("Books", "Fiction"),
        images = listOf("https://example.com/images/scifi_novel.jpg",),
        title = "The Last Starship (Signed Edition)",
        rating = null, // No rating yet
        price = 24.99,
        categories = listOf("Books"),
        description = "A thrilling new science fiction novel by A. M. Jones.",
        stock = 50.0,
        metadata = emptyMap(), // Empty metadata
        isDiscounted = false,
        discount = null
    )

    // 5. A service with all optional properties set to null (minimal valid object)
    val demoMinimalService =
        _root_ide_package_.com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto(
            id = "svc_999",
            templateId = null,
            images = listOf(),
            title = "Quick Setup Service",
            rating = null,
            price = 25.00,
            description = null,
            stock = null,
            // metadata defaults to emptyMap()
            isDiscounted = null,
            discount = null
        )

    val demoItems = listOf(
        demoProduct,
        demoServiceDiscounted,
        demoProductOutOfStock,
        demoProductNew,
        demoMinimalService
    )

}