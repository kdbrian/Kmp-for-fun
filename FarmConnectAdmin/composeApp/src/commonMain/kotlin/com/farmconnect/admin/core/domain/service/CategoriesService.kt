package com.farmconnect.admin.core.domain.service

interface CategoriesService {
    val collectionName: String get() = "com:farmconnect/categories/default"
    suspend fun loadCategories(): Result<List<com.farmconnect.admin.core.domain.dto.CategoryItemDto>>
    suspend fun addCategory(categoryItemDto: com.farmconnect.admin.core.domain.dto.CategoryItemDto): Result<Boolean>
    suspend fun searchCategory(query: String): Result<List<com.farmconnect.admin.core.domain.dto.CategoryItemDto>>
}