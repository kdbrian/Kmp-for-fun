package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface NotificationService {
    suspend fun loadNotifications(): Flow<List<com.farmconnect.admin.core.domain.model.NotificationRecordDomain>>
    suspend fun saveNotification(notificationRecord: com.farmconnect.admin.core.domain.model.NotificationRecord)
    suspend fun deleteNotification(notificationId: String): Boolean
}