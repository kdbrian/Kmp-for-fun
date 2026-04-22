package com.farmconnect.admin.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val product: com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto,
    val count: Int,
    val description: String? = null
)
