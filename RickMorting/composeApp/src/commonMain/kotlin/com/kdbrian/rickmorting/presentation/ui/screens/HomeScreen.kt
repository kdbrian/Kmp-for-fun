package com.kdbrian.rickmorting.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.rickmorting.AllCharactersQuery
import com.kdbrian.rickmorting.presentation.ui.components.CharacterCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    characters: List<AllCharactersQuery.Result?> = emptyList()
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item(span = { GridItemSpan(maxCurrentLineSpan) }) { }

        items(characters) { character ->
            CharacterCard(
                character = character
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
    HomeScreen()
}
