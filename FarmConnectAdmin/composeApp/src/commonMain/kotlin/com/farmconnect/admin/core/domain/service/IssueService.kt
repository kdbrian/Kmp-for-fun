package com.farmconnect.admin.core.domain.service

interface IssueService {
    val collection: String
    suspend fun reportIssue(issue: com.farmconnect.admin.core.domain.model.BugReport): Result<String>
}