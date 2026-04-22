package com.farmconnect.admin.core.domain.service

interface BusinessService {

    val collectionName: String
        get() = "com:farmconnect/businesses_v2/metadata"

    suspend fun businessAccountExistsByName(businessName: String): Result<Boolean>
    suspend fun loadBusinessAccountForUser(userId: String): Result<com.farmconnect.admin.core.domain.model.BusinessAccount>
    suspend fun loadBusinessAccount(businessId: String): Result<com.farmconnect.admin.core.domain.model.BusinessAccount>
    suspend fun addBusinessAccount(businessAccountDto: com.farmconnect.admin.core.domain.dto.BusinessAccountDto): Result<Boolean>
    suspend fun updateBusinessAccount(
        businessId: String,
        builder: com.farmconnect.admin.core.domain.model.BusinessAccount.Builder
    ): Result<Boolean>

    suspend fun deleteBusinessAccount(businessId: String): Result<Boolean>
    suspend fun reloadAccount(businessId: String): Result<Boolean>

}