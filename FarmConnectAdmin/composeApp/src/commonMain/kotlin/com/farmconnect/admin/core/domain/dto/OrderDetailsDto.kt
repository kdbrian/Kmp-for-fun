package com.farmconnect.admin.core.domain.dto

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.time.Instant

@Serializable
data class OrderDetailsDto(
    val orderId: String = UUID.randomUUID().toString().split("-").first(),
    val userId: String? = null,
    val products: List<Map<String, String>> = emptyList(), // product(Id, quantity, price, discount, title, imageUrl, total) count
    val status: String = "",
    val contactDetails: com.farmconnect.admin.core.domain.model.ContactDetailsDomain? = null,
    val productsTotal: Double = 0.0,
    val deliveryTotal: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val total: Double = 0.0,
    val placeOn: Long = Instant.now().epochSeconds,
    val updatedOn: Long = Instant.now().epochSeconds,
    val metadata: Map<String, String> = emptyMap()
) {
    companion object {
        val demoPendingOrderDetailsDto =
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
                userId = "demo_user_id",
                products = listOf(
                    mapOf(
                        "product_id" to "prod_001",
                        "count" to "1",
                        "price" to "1000",
                        "discount" to "100",
                        "productTitle" to "Demo Product",
                        "imageUrl" to "https://via.placeholder.com/150",
                    )
                ),
                status = "Pending",
                productsTotal = 1000.0,
                deliveryTotal = 100.0,
                totalDiscount = 100.0,
            )
        val demoProcessedOrderDetailsDto =
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
                userId = "demo_user_id",
                products = listOf(
                    mapOf(
                        "product_id" to "prod_001",
                        "count" to "1",
                        "price" to "1000",
                        "discount" to "100",
                        "productTitle" to "Demo Product",
                        "imageUrl" to "https://via.placeholder.com/150",
                    )
                ),
                status = "Processed",
                productsTotal = 1000.0,
                deliveryTotal = 100.0,
                totalDiscount = 100.0,
            )

        val demoCompletedOrderDetailsDto =
            _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
                userId = "demo_user_id",
                products = listOf(
                    mapOf(
                        "product_id" to "prod_001",
                        "count" to "1",
                        "price" to "1000",
                        "discount" to "100",
                        "productTitle" to "Demo Product",
                        "imageUrl" to "https://via.placeholder.com/150",
                    )
                ),
                status = "Completed",
                productsTotal = 1000.0,
                deliveryTotal = 100.0
            )

    }
}


fun com.farmconnect.admin.core.domain.dto.OrderDetailsDto.toDetails() =
    _root_ide_package_.com.farmconnect.admin.core.domain.model.OrderDetails(
        orderId = orderId,
        userId = userId ?: "",
        productsTotal = productsTotal,
        deliveryTotal = deliveryTotal,
        totalDiscount = totalDiscount,
        total = total,
        status = status,
        products = products,
        contactDetails = contactDetails,
        metadata = metadata,
        placeOn = placeOn,
        updatedOn = updatedOn,
    )
