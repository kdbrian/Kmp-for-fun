package com.farmconnect.admin.core.domain.service

interface NotificationTokenService {


    val collectionName: String
        get() = "com:farmconnect/tokens/notifications"

    suspend fun uploadToken(tokenStore: com.farmconnect.admin.core.domain.model.NotificationTokenStore): Result<Boolean>
}