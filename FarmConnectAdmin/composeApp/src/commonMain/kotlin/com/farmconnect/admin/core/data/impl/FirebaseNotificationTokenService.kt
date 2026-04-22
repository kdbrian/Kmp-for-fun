package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.model.NotificationTokenStore
import com.farmconnect.admin.core.domain.service.NotificationTokenService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseNotificationTokenService : NotificationTokenService {

    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/ntokens/metadata"

    private val collection = FirebaseFirestore.getInstance().collection(collectionName)

    override suspend fun uploadToken(tokenStore: NotificationTokenStore): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                collection.document(tokenStore.userId).set(tokenStore, SetOptions.merge()).await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}