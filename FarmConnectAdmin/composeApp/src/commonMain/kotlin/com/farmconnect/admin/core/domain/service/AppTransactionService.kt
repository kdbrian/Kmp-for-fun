package com.farmconnect.admin.core.domain.service

import kotlinx.coroutines.flow.Flow

interface AppTransactionService {
    val path: String
        get() = "com_farmconnect/transactions_v2/default"

    suspend fun saveTransaction(transactionDto: com.farmconnect.admin.core.domain.dto.TransactionDto): Result<String>
    suspend fun loadTransactionsFor(transactionFor: String): Flow<Result<List<com.farmconnect.admin.core.domain.model.AppTransaction>>>
}