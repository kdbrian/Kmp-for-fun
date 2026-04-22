package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface DeliveryInfoService {

    suspend fun deliveryInfos() : Flow<List<com.farmconnect.admin.core.domain.model.DeliveryDetails>>

    suspend fun saveDeliveryInfo(deliveryInfoDto: com.farmconnect.admin.core.domain.dto.DeliveryInfoDto)

    suspend fun deleteDeliveryInfo(id : String) : Boolean

}