package com.farmconnect.admin.core.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.admin.core.domain.dto.toEntity
import com.farmconnect.admin.core.domain.model.ProductOrServiceItem
import com.farmconnect.admin.core.domain.model.toDto
import com.farmconnect.admin.core.domain.service.ProductService
import com.farmconnect.admin.core.util.dispatchIO
import com.farmconnect.admin.core.util.dispatchIOResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.farmconnect.admin.core.data.impl.paging.FilteredProductPagingSource
import com.farmconnect.admin.core.data.impl.paging.ProductPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import Napie.log.Napie


class FirebaseProductServiceImpl : ProductService {

    override val collectionName: String
        get() = "${BuildConfig.ref_base}_${BuildConfig.db_env}/products/metadata"

    //SUPPRESS : MissingPermission
    private val collection = FirebaseFirestore.getInstance().collection(collectionName)

    override suspend fun addProduct(productOrServiceItemDto: ProductOrServiceItemDto): Result<String> =
        dispatchIOResult {
            val documentReference = collection.document()
            documentReference
                .set(
                    productOrServiceItemDto.toEntity()
                        .copy(id = documentReference.id, createdOn = System.currentTimeMillis())
                ).await()
            documentReference.id
        }

    override suspend fun listProductLive(): Flow<Result<List<ProductOrServiceItemDto>>> =
        // 1. Use callbackFlow for callback-based APIs
        callbackFlow {
            // 2. Register the listener and get a Disposable/Registration object
            val registration = collection.addSnapshotListener { snap, error ->
                // 3. 'trySend' is used inside callbackFlow instead of 'emit'
                if (error != null) {
                    // Send failure and close the flow (optional: close on first error)
                    trySend(Result.failure(error)).isSuccess
                    close(error) // Closes the flow cleanly
                    return@addSnapshotListener
                }

                if (snap?.isEmpty?.not() == true) {
                    val productOrServiceItems =
                        snap.documents.mapNotNull { it.toObject(ProductOrServiceItem::class.java) }
                            .map { it.toDto() }
                    // Send the successful result
                    trySend(Result.success(productOrServiceItems)).isSuccess
                } else if (snap?.metadata?.isFromCache == false && snap.isEmpty) {
                    // Optionally handle an empty but up-to-date query
                    trySend(Result.success(emptyList())).isSuccess
                }
            }

            // 4. Await Close (Cleanup): This is called when the Flow is cancelled or completes
            awaitClose {
                // Remove the listener to prevent memory leaks
                registration.remove()
            }
        }

    override suspend fun listProductLiveWithName(name: String): Flow<Result<List<ProductOrServiceItemDto>>> =
        callbackFlow {
            val listener = collection.whereEqualTo("name", name)
                .addSnapshotListener { snap, error ->
                    if (error != null) {
                        // Send failure and close the flow (optional: close on first error)
                        trySend(Result.failure(error)).isSuccess
                        close(error) // Closes the flow cleanly
                        return@addSnapshotListener
                    }

                    trySend(Result.failure(Exception("Incomplete implementation.")))
                }


            awaitClose {
                listener.remove()
            }

        }


    override suspend fun listProducts(): Result<List<ProductOrServiceItem>> = dispatchIOResult {
        val queryDocumentSnapshots =
            collection
                .get()
                .await().toObjects(ProductOrServiceItem::class.java)
        Napie.d("received : ${queryDocumentSnapshots.size}")
        queryDocumentSnapshots
    }

    override suspend fun listProductById(productId: String): Result<ProductOrServiceItem> =
        dispatchIO {
            val documentSnapshot = collection.document(productId).get().await()

            if (!documentSnapshot.exists()) {
                Result.failure(Exception("Missing product wit passed id."))
            } else {
                val productOrServiceItem =
                    documentSnapshot.toObject(ProductOrServiceItem::class.java)
                Result.success(productOrServiceItem!!)
            }
        }


    override suspend fun searchProductListing(query: String): Result<List<ProductOrServiceItem>> =
        dispatchIO {
            listProducts()
                .onSuccess {
                    it.filter {
                        it.title?.contains(query, true) == true ||
                                it.description?.contains(
                                    query,
                                    true
                                ) == true || it.tags?.any {
                            it.equals(
                                query,
                                true
                            )
                        } == true
                    }
                }
        }

    override suspend fun updateProduct(
        productId: String,
        updates: Map<String, Any?>
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val documentReference = collection.document(productId)
            documentReference
                .set(updates.apply {
                    toMutableMap().apply {
                        set("updatedOn", System.currentTimeMillis())
                    }
                }, SetOptions.merge())
                .await()

            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProductById(productId: String): Result<Boolean> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                collection
                    .document(productId)
                    .delete()
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteProductsWithNameContaining(
        name: String,
        searchFields: List<String>
    ): Result<Long> = withContext(Dispatchers.IO) {
        try {
            Result.success(0L)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun listProductsPaged(
        pageSize: Int,
        category: String?
    ): Flow<PagingData<ProductOrServiceItemDto>> {
        var firestoreQuery = collection.orderBy("createdOn", Query.Direction.DESCENDING)

        if (!category.isNullOrEmpty()) {
            Napie.d("category $category")
            firestoreQuery = firestoreQuery.whereArrayContains("categories", category)
        }

        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductPagingSource(firestoreQuery, pageSize.toLong())
            }
        ).flow
    }

    override fun listProductsByValue(
        pageSize: Int,
        value: Pair<String, Any?>
    ): Flow<PagingData<ProductOrServiceItemDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FilteredProductPagingSource(
                    collection = collection, // Your Firestore collection reference
                    filterKey = value.first,
                    filterValue = value.second,
                    pageSize = pageSize.toLong()
                )
            }
        ).flow
    }
}