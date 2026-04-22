package com.farmconnect.admin.core.domain.dto

import kotlin.time.Instant

data class TransactionDto(
    val transactionId: String? = "",
    val transactionBy: String? = "",
    val transactionAt: String? = "", //affected resource pat
    val transactionFor: String = "", //classname updated entity(s)->separated by '#'
    val desc: String? = "", //classname updated entity(s)->separated by '#'
    val createdAt: Long = Instant.now().epochSeconds,
) {
    class Builder {
        private var transactionId: String? = ""
        private var transactionBy: String? = ""
        private var transactionAt: String? = "" //affected resource pat
        private var transactionFor: String = "" //classname updated entity(s)->separated by '#'
        private var desc: String? = "" //classname updated entity(s)->separated by '#'
        private var createdAt: Long = Instant.now().epochSeconds

        fun transactionId(transactionId: String) = apply { this.transactionId = transactionId }
        fun transactionBy(transactionBy: String) = apply { this.transactionBy = transactionBy }
        fun transactionAt(transactionAt: String) = apply { this.transactionAt = transactionAt }
        fun transactionFor(transactionFor: String) = apply { this.transactionFor = transactionFor }
        fun desc(desc: String) = apply { this.desc = desc }
        fun createdAt(createdAt: Long) = apply { this.createdAt = createdAt }


        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.dto.TransactionDto(
            transactionId = transactionId,
            transactionBy = transactionBy,
            transactionAt = transactionAt,
            transactionFor = transactionFor,
            desc = desc,
            createdAt = createdAt
        )

    }
}


fun com.farmconnect.admin.core.domain.dto.TransactionDto.toEntity() =
    _root_ide_package_.com.farmconnect.admin.core.domain.model.AppTransaction(
        transactionBy = transactionBy.toString(),
        transactionAt = transactionAt.toString(),
        transactionFor = transactionFor,
        desc = desc,
        createdAt = createdAt
    )