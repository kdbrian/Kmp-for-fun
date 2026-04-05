package com.kdbrian.rickmorting

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kdbrian.rickmorting.presentation.ui.screens.HomeScreen
import com.kdbrian.rickmorting.presentation.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    mainViewModel: MainViewModel = koinViewModel()
) {

    val characters = mainViewModel.characters.collectAsStateWithLifecycle()


    MaterialTheme {

        HomeScreen(
            modifier = Modifier.safeDrawingPadding(),
            characters = characters.value ?: emptyList()
        )
    }
}