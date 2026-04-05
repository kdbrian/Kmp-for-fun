package com.kdbrian.rickmorting.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kdbrian.rickmorting.AllCharactersQuery
import org.jetbrains.compose.resources.painterResource
import rickmorting.composeapp.generated.resources.Res
import rickmorting.composeapp.generated.resources.placeholder

@Composable
fun CharacterCard(
    modifier: Modifier = Modifier,
    character: AllCharactersQuery.Result? = null
) {
    Box(
        modifier = modifier.size(250.dp),
        contentAlignment = Alignment.Center
    ) {

        AsyncImage(
            model = character?.image,
            contentDescription = character?.name,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(Res.drawable.placeholder),
            error = painterResource(Res.drawable.placeholder),
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier.fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(.65f),
                        )
                    )
                )
        )


        Surface(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            color = Color(0xFFE4572E)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = character?.name ?: LoremIpsum(2).values.joinToString(),
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = character?.gender ?: LoremIpsum(2).values.joinToString(),
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = character?.status ?: LoremIpsum(2).values.joinToString(),
                        style = MaterialTheme.typography.labelMedium
                    )

                }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CharacterCardPrev() {
    MaterialTheme {
        CharacterCard()
    }
}
