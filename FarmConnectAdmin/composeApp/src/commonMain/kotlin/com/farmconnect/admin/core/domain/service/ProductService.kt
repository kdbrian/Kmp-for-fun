package com.farmconnect.admin.core.domain.service

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ProductService {
    val collectionName: String get() = "com:farmconnect/products_v2/metadata"
    suspend fun addProduct(productOrServiceItemDto: com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto): Result<String>
    suspend fun listProductLive(): Flow<Result<List<com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto>>>

    suspend fun listProductLiveWithName(name: String): Flow<Result<List<com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto>>>

    suspend fun listProducts(): Result<List<com.farmconnect.admin.core.domain.model.ProductOrServiceItem>>
    fun listProductsByValue(
        pageSize: Int,
        value: Pair<String, Any?>
    ): Flow<PagingData<com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto>>
    suspend fun listProductById(productId: String): Result<com.farmconnect.admin.core.domain.model.ProductOrServiceItem>
    suspend fun searchProductListing(query: String): Result<List<com.farmconnect.admin.core.domain.model.ProductOrServiceItem>>
    suspend fun updateProduct(
        productId: String,
        updates: Map<String, Any?>
    ): Result<Boolean>

    suspend fun deleteProductById(productId: String): Result<Boolean>
    suspend fun deleteProductsWithNameContaining(
        name: String,
        searchFields: List<String>
    ): Result<Long>

    fun listProductsPaged(
     pageSize: Int = 20,
     category: String?=null // New optional parameter
    ): Flow<PagingData<com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto>>
}