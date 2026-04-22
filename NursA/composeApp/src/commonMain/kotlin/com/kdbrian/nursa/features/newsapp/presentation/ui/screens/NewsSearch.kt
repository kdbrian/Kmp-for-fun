package com.kdbrian.nursa.features.newsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.nursa.config.util.Resource
import com.kdbrian.nursa.features.newsapp.domain.dto.NewsResponse
import com.kdbrian.nursa.features.newsapp.presentation.components.NewsCard

@Composable
fun NewsSearch(
    modifier: Modifier = Modifier,
    newsResource: Resource<NewsResponse> = Resource.Idle(),
    query: String = "",
    onQueryChange: (String) -> Unit = {}
) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(3),
        modifier = modifier.fillMaxSize().padding(12.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item(
            span =
                StaggeredGridItemSpan.FullLine
        ) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null
                    )
                },
                enabled = newsResource !is Resource.Loading,
                placeholder = {
                    Text(
                        text = "Search news..."
                    )
                }
            )
        }

        when (newsResource) {
            is Resource.Success -> {
                newsResource.data?.let { newsResponse ->
                    this@LazyVerticalStaggeredGrid.items(newsResponse.news) { news ->
//                            val news = newsResponse.news[idx]
                        NewsCard(
                            modifier = Modifier.fillMaxWidth(),
                            news = news
                        )
                    }
                }
            }

            else -> {}
        }


    }
}

@Composable
@Preview
fun NewsSearchPreview() {
    NewsSearch()

}
