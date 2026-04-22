package com.farmconnect.admin.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AppTransaction(
    val transactionId: String="",
    val transactionBy: String="",
    val transactionAt: String="",
    val transactionFor: String="",
    val desc: String?="",
    val createdAt: Long= Instant.now().epochSeconds,
)

fun com.farmconnect.admin.core.domain.model.AppTransaction.toDto() =
    _root_ide_package_.com.farmconnect.admin.core.domain.dto.TransactionDto(
        transactionId = transactionId,
        transactionBy = transactionBy,
        transactionAt = transactionAt,
        transactionFor = transactionFor,
        desc = desc,
        createdAt = createdAt
    )
