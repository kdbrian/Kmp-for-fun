package com.farmconnect.admin.core.domain.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
class NotificationTokenStore internal constructor(
    val userId: String,
    val tokens: String,
    val updatedAt: Long = Instant.now().epochSeconds
) {
    class Builder {
        private var userId: String = ""
        private var tokens: String = ""

        fun userId(userId: String) = apply { this.userId = userId }
        fun token(tokens: String) = apply { this.tokens = tokens }

        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.model.NotificationTokenStore(
            userId,
            tokens
        )
    }
}
