package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface PaymentService {
    val dbRef : String
    suspend fun requestsMpesaPayment(ref : String): Result<com.farmconnect.admin.core.domain.dto.PaymentUtils.MpesaPaymentResponse>
    suspend fun loadRequestsByCommodityId(id: String): Result<com.farmconnect.admin.core.domain.dto.PaymentUtils.MPesaPaymentRecord?>
    suspend fun loadRequestsBySubject(subject: String): Flow<List<com.farmconnect.admin.core.domain.dto.PaymentUtils.MPesaPaymentRecord>>
    suspend fun savePaymentRequest(payment: com.farmconnect.admin.core.domain.dto.PaymentUtils.MPesaPaymentRecord.Builder): Result<String>
    suspend fun updatePaymentRequest(
        id: String,
        updates: Map<String, Any>
    ): Result<String>
}