package com.farmconnect.admin.core.domain.model

import com.google.firebase.auth.FirebaseUser
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.time.Instant

@Serializable
class AppUser internal constructor(
    val email: String? = null,
    val phone: String? = null,
    val modes: MutableList<String> = mutableListOf(),
    val metadata: Map<String, String> = mapOf(),
    val uid: String = UUID.randomUUID().toString(),
    val createAt: Long = Instant.now().epochSeconds
) {
    class Builder {
        private var email: String? = null
        private var modes: MutableList<String> = mutableListOf()
        private var phone: String? = null
        var uid: String? = null
        private var metadata: Map<String, String> = mapOf()

        fun setEmail(userEmail: String?) = apply { email = userEmail }

        fun setPhone(userPhone: String?) = apply { phone = userPhone }
        fun setUid(userId: String?) = apply { uid = userId }
        fun setMode(userMode: String) = apply { modes.add(userMode) }
        fun setModes(userModes: MutableList<String>) = apply { modes.addAll(userModes) }
        fun setMetadata(userMetadata: Map<String, String>) = apply { metadata = userMetadata }

        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.model.AppUser(
            email = email,
            phone = phone,
            uid = uid ?: UUID.randomUUID().toString().split("-").joinToString().substring(12),
            modes = modes,
            metadata = metadata
        )


    }

    override fun toString(): String {
        return "AppUser(email=$email, phone=$phone, modes=$modes, metadata=$metadata, uid=$uid)"
    }
}

fun UserInfo.toAppUser() = _root_ide_package_.com.farmconnect.admin.core.domain.model.AppUser.Builder()
    .setUid(id)
    .setEmail(email)
    .setPhone(phone)


fun FirebaseUser.toAppUser() = _root_ide_package_.com.farmconnect.admin.core.domain.model.AppUser.Builder()
    .setUid(uid)
    .setEmail(email)
    .setPhone(phoneNumber)
    .setMetadata(
        mapOf(
            "displayName" to displayName.toString(),
            "photoUrl" to photoUrl.toString(),
        )
    )
