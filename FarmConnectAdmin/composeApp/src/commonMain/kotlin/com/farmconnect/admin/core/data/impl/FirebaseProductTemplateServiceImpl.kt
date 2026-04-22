package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.dto.ProductTemplateDto
import com.farmconnect.admin.core.domain.model.ProductTemplate
import com.farmconnect.admin.core.domain.model.toDto
import com.farmconnect.admin.core.domain.service.ProductTemplateService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseProductTemplateServiceImpl : ProductTemplateService {

    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/templates/metadata"

    private val collection = FirebaseFirestore.getInstance().collection(collectionName)

    override fun loadTemplates(): Flow<Result<List<ProductTemplateDto>>> = callbackFlow {

        val snapshotListener = collection.addSnapshotListener { value, error ->
            // 3. 'trySend' is used inside callbackFlow instead of 'emit'
            if (error != null) {
                // Send failure and close the flow (optional: close on first error)
                trySend(Result.failure(error)).isSuccess
                close(error) // Closes the flow cleanly
                return@addSnapshotListener
            }

            if (value?.isEmpty?.not() == true) {
                val productOrServiceItems =
                    value.documents.mapNotNull { it.toObject(ProductTemplate::class.java)?.toDto() }
                // Send the successful result
                trySend(Result.success(productOrServiceItems))
            } else if (value?.metadata?.isFromCache == false && value.isEmpty) {
                // Optionally handle an empty but up-to-date query
                trySend(Result.success(emptyList())).isSuccess
            }
        }

        awaitClose {
            snapshotListener.remove()
        }
    }


    override suspend fun saveTemplate(template: ProductTemplateDto): Result<Boolean> = withContext(
        Dispatchers.IO
    ) {
        try {
            val documentReference = collection.document()
            documentReference.set(
                template.copy(
                    templateId = documentReference.id
                )
            ).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun updateTemplate(
        templateId: String,
        template: ProductTemplateDto?
    ): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val documentReference = collection.document(templateId).get().await()
                if (documentReference.exists().not()) {
                    Result.failure(Exception("Template not found"))
                } else {
                    collection.document(templateId).set(
                        template!!.copy(
                            updatedOn = System.currentTimeMillis()
                        ), SetOptions.merge()
                    ).await()
                    Result.success(template.templateId)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        }
}