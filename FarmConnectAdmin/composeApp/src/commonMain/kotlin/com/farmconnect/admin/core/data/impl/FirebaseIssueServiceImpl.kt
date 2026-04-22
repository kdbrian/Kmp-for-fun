package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.model.BugReport
import com.farmconnect.admin.core.domain.service.IssueService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseIssueServiceImpl : IssueService {

    override val collection: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/issues/report"


    private val dbRef = FirebaseFirestore.getInstance().collection(collection)

    override suspend fun reportIssue(issue: BugReport): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val documentReference = dbRef.document(issue.id)
                documentReference
                    .set(issue)
                    .await()
                Result.success(issue.id)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}