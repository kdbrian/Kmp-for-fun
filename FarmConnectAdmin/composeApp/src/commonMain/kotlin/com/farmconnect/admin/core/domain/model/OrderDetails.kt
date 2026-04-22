package com.farmconnect.admin.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class OrderDetails(
    val orderId: String = "",
    val userId: String = "",
    val productsTotal: Double = 0.0,
    val deliveryTotal: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val total: Double = 0.0,
    val status: String = "",
    val products: List<Map<String, String>> = emptyList(),
    val contactDetails: com.farmconnect.admin.core.domain.model.ContactDetailsDomain? = null,
    val metadata: Map<String, String> = emptyMap(), // deliveredOn(Long), dispatchedOn(Long) //business
    val placeOn: Long = Instant.now().epochSeconds,
    val updatedOn: Long = Instant.now().epochSeconds,
)

fun com.farmconnect.admin.core.domain.model.OrderDetails.toDto() =
    _root_ide_package_.com.farmconnect.admin.core.domain.dto.OrderDetailsDto(
        userId = userId,
        orderId = orderId,
        products = products,
        status = status,
        contactDetails = contactDetails,
        productsTotal = productsTotal,
        deliveryTotal = deliveryTotal,
        totalDiscount = totalDiscount,
        total = total,
        placeOn = placeOn,
        updatedOn = updatedOn,
        metadata = metadata,
    )
