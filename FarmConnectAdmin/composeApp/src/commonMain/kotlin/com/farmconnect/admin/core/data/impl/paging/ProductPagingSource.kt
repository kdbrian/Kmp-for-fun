package com.farmconnect.admin.core.data.impl.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.farmconnect.admin.core.domain.dto.ProductOrServiceItemDto
import com.farmconnect.admin.core.domain.model.ProductOrServiceItem
import com.farmconnect.admin.core.domain.model.toDto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import Napie.log.Napie

class ProductPagingSource(
    private val query: Query,
    private val pageSize: Long = 20
) : PagingSource<DocumentSnapshot, ProductOrServiceItemDto>() {

    override suspend fun load(
        params: LoadParams<DocumentSnapshot>
    ): LoadResult<DocumentSnapshot, ProductOrServiceItemDto> {
        return try {
            val currentPage = params.key

            // We use the 'query' passed in as the base
            val currentQuery = if (currentPage == null) {
                query.limit(pageSize)
            } else {
                query.startAfter(currentPage).limit(pageSize)
            }

            val snapshot = currentQuery.get().await()
            val items = snapshot.documents
                .mapNotNull { it.toObject(ProductOrServiceItem::class.java)?.toDto() }

            Napie.d("items $currentQuery ${items.size}")

            val lastVisible = snapshot.documents.lastOrNull()

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = if (items.size < pageSize) null else lastVisible
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(
        state: PagingState<DocumentSnapshot, ProductOrServiceItemDto>
    ): DocumentSnapshot? = null
}