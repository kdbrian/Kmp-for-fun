package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface OrderService {

    val collectionName: String get() = "com:farmconnect/orders_v2/default"

    suspend fun placeOrder(orderDto: com.farmconnect.admin.core.domain.dto.OrderDetailsDto): Result<String>

    suspend fun getAllOrders(): Flow<Result<List<com.farmconnect.admin.core.domain.model.OrderDetails>>>

    suspend fun loadOrdersByKey(key: String, value: String): Flow<Result<List<com.farmconnect.admin.core.domain.model.OrderDetails>>>
    suspend fun loadOrderById(id: String): Result<com.farmconnect.admin.core.domain.model.OrderDetails?>

}