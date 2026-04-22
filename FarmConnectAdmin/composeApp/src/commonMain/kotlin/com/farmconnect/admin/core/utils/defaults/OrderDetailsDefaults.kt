package com.farmconnect.admin.core.utils.defaults

import kotlin.time.Instant

object OrderDetailsDefaults {

    // A standard set of product data for the 'products' list (List<Map<String, String>>)
    private val standardProductsList = listOf(
        mapOf(
            "productId" to "prod_001",
            "name" to "4K Monitor",
            "quantity" to "1",
            "price" to "499.99"
        ),
        mapOf(
            "productId" to "prod_015",
            "name" to "Sci-Fi Novel",
            "quantity" to "2",
            "price" to "24.99"
        )
    )

    // Standard contact details
    private val standardContact =
        _root_ide_package_.com.farmconnect.admin.core.domain.model.ContactDetailsDomain(
//        name = "Jane Doe",
            phoneNumber = "555-0101",
            locationName = "123 Test Street, Anytown, CA 90210"
        )


    // 1. A complete, successfully placed order (PROCESSING)
    val ORDER_PROCESSING = _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
        orderId = "ORD-A1B2C3",
        userId = "user_456",
        products = standardProductsList,
        status = "PROCESSING",
        contactDetails = standardContact,
        productsTotal = 549.97, // 499.99 + (2 * 24.99)
        deliveryTotal = 15.00,
        totalDiscount = 0.0,
        total = 564.97, // 549.97 + 15.00
        placeOn = 1733683200000L, // Dec 9, 2025
        updatedOn = Instant.now().epochSeconds,
        metadata = mapOf("payment_method" to "Credit Card", "shipping_method" to "Standard")
    )

    // 2. An order with a discount applied (SHIPPED)
    val ORDER_DISCOUNTED_SHIPPED =
        _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
            orderId = "ORD-D4E5F6",
            userId = "user_789",
            products = standardProductsList,
            status = "SHIPPED",
            contactDetails = standardContact,
            productsTotal = 549.97,
            deliveryTotal = 10.00,
            totalDiscount = 50.00,
            total = 509.97, // 549.97 + 10.00 - 50.00
            placeOn = 1733596800000L, // Dec 8, 2025
            updatedOn = Instant.now().epochSeconds,
            metadata = mapOf("coupon_code" to "FALL50", "tracking_number" to "TRK1234567")
        )

    // 3. A cancelled order (missing contact details, minimal metadata)
    val ORDER_CANCELLED = _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
        orderId = "ORD-G7H8I9",
        userId = "user_101",
        products = listOf(
            mapOf(
                "productId" to "prod_010",
                "name" to "Linen Shirt",
                "quantity" to "1",
                "price" to "59.99"
            )
        ),
        status = "CANCELLED",
        contactDetails = null, // Contact details might be null if order was cancelled very early
        productsTotal = 59.99,
        deliveryTotal = 0.0,
        totalDiscount = 0.0,
        total = 59.99,
        placeOn = 1733510400000L, // Dec 7, 2025
        updatedOn = Instant.now().epochSeconds,
        metadata = emptyMap()
    )

    // 4. A large, high-value order
    val ORDER_HIGH_VALUE = _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
        orderId = "ORD-J0K1L2",
        userId = "user_vip",
        products = listOf(
            mapOf(
                "productId" to "prod_001",
                "name" to "4K Monitor",
                "quantity" to "5",
                "price" to "499.99"
            ),
            mapOf(
                "productId" to "svc_005",
                "name" to "Consultation",
                "quantity" to "1",
                "price" to "150.00"
            )
        ),
        status = "DELIVERED",
        contactDetails = standardContact,
        productsTotal = 2649.95, // (5 * 499.99) + 150.00
        deliveryTotal = 0.0, // Free shipping
        totalDiscount = 100.00,
        total = 2549.95, // 2649.95 - 100.00
        placeOn = 1732992000000L, // Dec 1, 2025
        updatedOn = Instant.now().epochSeconds,
        metadata = mapOf("is_wholesale" to "true", "priority" to "High")
    )

    val ALL_ORDERS = listOf(
        ORDER_PROCESSING,
        ORDER_DISCOUNTED_SHIPPED,
        ORDER_CANCELLED,
        ORDER_HIGH_VALUE
    )
}