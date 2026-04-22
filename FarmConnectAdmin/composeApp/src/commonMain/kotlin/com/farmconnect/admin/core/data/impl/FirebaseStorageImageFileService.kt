package com.farmconnect.admin.core.data.impl

import android.net.Uri
import com.farmconnect.admin.core.domain.service.FileService
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import Napie.log.Napie

class FirebaseStorageImageFileService : FileService {
    override val path: String = "${BuildConfig.ref_base}_${BuildConfig.db_env}/public/uploads"

    private val storage = FirebaseStorage.getInstance()

    override suspend fun uploadImage(imageUri: Uri): Result<String> {
        return try {
            val storageReference = storage.getReference(path).child(
                "${System.currentTimeMillis()}_${imageUri.lastPathSegment}"
            )
            val uploadTaskTaskSnapshot = storageReference.putFile(imageUri).await()
            if (uploadTaskTaskSnapshot.task.isComplete) {
                val uri = uploadTaskTaskSnapshot.storage.downloadUrl.await()
                Napie.d("Uploaded image to $uri")
                Result.success(uri.toString())
            } else
                Result.failure(Exception("Upload failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteImage(imageUri: String): Result<Boolean> {
        return try {
            val storageReference = storage.getReferenceFromUrl(imageUri)
            storageReference.delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}