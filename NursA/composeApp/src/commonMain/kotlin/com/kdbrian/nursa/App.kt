package com.kdbrian.nursa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kdbrian.nursa.features.newsapp.NewsViewModel
import com.kdbrian.nursa.features.newsapp.presentation.ui.screens.NewsSearch
import org.jetbrains.compose.resources.painterResource

import nursa.composeapp.generated.resources.Res
import nursa.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {

    val newsViewModel : NewsViewModel  = koinViewModel()

    val newsResource = newsViewModel.newsResource.collectAsState()

    MaterialTheme {
        NewsSearch(
            newsResource = newsResource.value,
            query = newsViewModel.query.collectAsState().value,
            onQueryChange = newsViewModel::updateQuery,
        )
    }
}