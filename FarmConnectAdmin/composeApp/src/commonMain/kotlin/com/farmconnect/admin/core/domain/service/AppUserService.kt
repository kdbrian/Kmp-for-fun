package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface AppUserService {
    val collectionName: String get() = "com:farmconnect/users_v2/metadata"

    val isEmailVerified : Boolean
    val currentUser: Flow<com.farmconnect.admin.core.domain.model.AppUser?>
    suspend fun profileById(id: String) : Result<com.farmconnect.admin.core.domain.model.AppUser?>
    suspend fun updateName(name: String): Result<Boolean>
    suspend fun updateAccount(
        id : String,
        updated : Map<String, Any>
    ) : Result<Boolean>

    suspend fun userExistWithEmailOrPhoneNumber(email: String?,phoneNumber: String?): Result<Boolean>


}