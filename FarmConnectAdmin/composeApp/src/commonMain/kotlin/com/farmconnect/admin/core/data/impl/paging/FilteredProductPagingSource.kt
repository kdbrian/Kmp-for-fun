package com.farmconnect.admin.core.data.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.admin.core.domain.model.ProductOrServiceItem
import com.farmconnect.admin.core.domain.model.toDto
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FilteredProductPagingSource(
    private val collection: CollectionReference,
    private val filterKey: String,
    private val filterValue: Any?,
    private val pageSize: Long = 20
) : PagingSource<DocumentSnapshot, ProductOrServiceItemDto>() {

    override suspend fun load(
        params: LoadParams<DocumentSnapshot>
    ): LoadResult<DocumentSnapshot, ProductOrServiceItemDto> {
        return try {
            val currentPage = params.key
            val query = if (currentPage == null) {
                collection
                    .whereEqualTo(filterKey, filterValue)
                    .orderBy("createdOn", Query.Direction.DESCENDING)
                    .limit(pageSize)
            } else {
                collection
                    .whereEqualTo(filterKey, filterValue)
                    .orderBy("createdOn", Query.Direction.DESCENDING)
                    .startAfter(currentPage)
                    .limit(pageSize)
            }

            val snapshot = query.get().await()
            val items = snapshot.documents
                .mapNotNull { it.toObject(ProductOrServiceItem::class.java)?.toDto() }

            val lastVisible = snapshot.documents.lastOrNull()

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = lastVisible
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<DocumentSnapshot, ProductOrServiceItemDto>
    ): DocumentSnapshot? = null
}