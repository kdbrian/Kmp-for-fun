package com.farmconnect.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.util.Resource

data class ResultsForUiState(
    val data: List<Any> = emptyList(),
    val title: String = ""
)

@Composable
fun ResultsFor(
    modifier: Modifier = Modifier,
    uiState: Resource<ResultsForUiState> = Resource.Idle(),
    onBack: () -> Unit = {},
    listContent: LazyListScope.() -> Unit = {
    }
) {

    Scaffold(
        topBar = {
            FarmConnectElevatedAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                parameters = FarmConnectElevatedAppBarPreviewParameters(
                    title = buildAnnotatedString {
                        append("Results for ${uiState.data?.title ?: "e"}")
                    },
                ),
            )
        }
    ) { pd ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(pd),
            verticalArrangement = if (uiState is Resource.Loading) Arrangement.Center else Arrangement.spacedBy(
                12.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                AnimatedContent(uiState) {
                    when (it) {
                        is Resource.Loading -> {
                            CircularProgressIndicator()
                        }

                        is Resource.Success -> this@LazyColumn.listContent()

                        else -> Unit
                    }
                }
            }
            listContent()
        }
    }

}

@Preview
@Composable
private fun ResultsForPrev() {
    ResultsFor()
}