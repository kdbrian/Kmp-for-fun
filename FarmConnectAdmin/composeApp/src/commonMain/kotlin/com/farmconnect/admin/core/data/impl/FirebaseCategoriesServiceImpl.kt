package com.farmconnect.admin.core.data.impl

import com.farmconnect.admin.core.domain.dto.CategoryItemDto
import com.farmconnect.admin.core.domain.dto.toDto
import com.farmconnect.admin.core.domain.model.CategoryItem
import com.farmconnect.admin.core.domain.service.CategoriesService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseCategoriesServiceImpl : CategoriesService {

    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/categories/metadata"

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun loadCategories(): Result<List<CategoryItemDto>> =
        withContext(Dispatchers.IO) {
            try {
                val documentSnapshots = firestore
                    .collection(collectionName)
                    .get()
                    .await()
                if (documentSnapshots == null)
                    Result.failure(Exception("Something unexpected happened."))
                else {
                    val categoryItems = documentSnapshots.toObjects(CategoryItem::class.java)
                    Result.success(categoryItems.map { item -> item.toDto() })
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun addCategory(categoryItemDto: CategoryItemDto): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val documentReference = firestore.collection(collectionName).document()
                documentReference.set(
                    CategoryItem
                        .Builder(categoryItemDto)
                        .categoryId(documentReference.id)
                        .createOn(System.currentTimeMillis())
                ).await()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun searchCategory(query: String): Result<List<CategoryItemDto>> = withContext(
        Dispatchers.IO
    ) {
        try {
            loadCategories()
                .onSuccess {
                    Result.success(
                        it.filter { dto ->
                            dto.categoryName.contains(query, true) ||
                                    dto.categoryDescription.contains(query, true) ||
                                    dto.tags.joinToString(",").contains(query, true)
                        }
                    )
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}