package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface ProductTemplateService {

    val collectionName: String
        get() = "${com.farmconnect.admin.core.domain.service.ProductService::collectionName}".replace("products", "templates")

    fun loadTemplates(): Flow<Result<List<com.farmconnect.admin.core.domain.dto.ProductTemplateDto>>>
    suspend fun saveTemplate(template: com.farmconnect.admin.core.domain.dto.ProductTemplateDto): Result<Boolean>
    suspend fun updateTemplate(
        templateId: String,
        template: com.farmconnect.admin.core.domain.dto.ProductTemplateDto? = null
    ): Result<String>
}
