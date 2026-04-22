package com.farmconnect.admin.core.domain.service

import android.net.Uri

interface FileService {

    val path : String
    suspend fun uploadImage(imageUri: Uri): Result<String>
    suspend fun deleteImage(imageUri: String): Result<Boolean>
}