package com.farmconnect.admin.core.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.time.Instant

interface Issue {
    val id: String
        get() = "Issue".plus(UUID.randomUUID().toString().split("-").first()).uppercase()
    val issueText: String
    val tags: List<String>
    val createdOn: Long
        get() = Instant.now().epochSeconds
    val updatedOn: Long
        get() = Instant.now().epochSeconds
}

@Serializable
data class BugReport(
    override val issueText: String,
    override val tags: List<String>
) : com.farmconnect.admin.core.domain.model.Issue {
    class Builder {


        var issueText: String? = null
        var tags: List<String>? = null

        fun issueText(issueText: String) = apply { this.issueText = issueText }
        fun tags(tags: List<String>) = apply { this.tags = tags }

        fun addTag(tag: String) = apply {
            tags = tags?.plus(tag)
        }

        fun build() = _root_ide_package_.com.farmconnect.admin.core.domain.model.BugReport(
            issueText = issueText ?: "",
            tags = tags ?: emptyList()
        )

    }
}