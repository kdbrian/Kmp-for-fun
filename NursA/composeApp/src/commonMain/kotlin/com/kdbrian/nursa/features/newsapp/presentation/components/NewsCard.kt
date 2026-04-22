package com.kdbrian.nursa.features.newsapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kdbrian.nursa.features.newsapp.domain.model.NewsData

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    news: NewsData? = null,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp)
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape
    ) {
        news?.let { data ->
            Column(
                modifier = Modifier,
//                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AsyncImage(
                    model = data.image,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp)
                )

                Column (
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    Text(
                        text = data.title ?: "",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(1f)
                    )

                    data.category?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(6.dp).background(
                                color = Color.Red.copy(.7f),
                                shape = RoundedCornerShape(50)
                            ).padding(
                                horizontal = 6.dp,
                                vertical = 2.dp
                            ),
                            color = Color.White
                        )
                    }

                }


                Text(
                    text = data.summary ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )


                IconButton({}) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null
                    )
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data found.",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }

}

@Composable
@Preview
fun NewsCardPreview() {
    NewsCard()

}