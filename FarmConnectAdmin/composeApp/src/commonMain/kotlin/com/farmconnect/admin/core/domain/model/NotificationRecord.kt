package com.farmconnect.admin.core.domain.model

import com.farmconnect.admin.core.util.NotificationUtils
import java.util.UUID
import kotlin.random.Random

interface NotificationRecord {
    val id: String
        get() = UUID.randomUUID()
            .toString()
            .split("-")
            .first()
            .uppercase()
    val title: String
//    val uid: String
    val message: String
    val bigText: String
}

fun NotificationUtils.NotificationData.toDomain() =
    _root_ide_package_.com.farmconnect.admin.core.domain.model.NotificationRecordDomain(
        id = Random(this.channelId.toIntOrNull() ?: 100).nextInt().toString(),
        title = title,
        message = message,
        bigText = bigText ?: "",
//    uid = channelId
    )


data class NotificationRecordDomain(
    override val id: String,
//    override val uid: String,
    override val title: String,
    override val message: String,
    override val bigText: String
) : com.farmconnect.admin.core.domain.model.NotificationRecord
