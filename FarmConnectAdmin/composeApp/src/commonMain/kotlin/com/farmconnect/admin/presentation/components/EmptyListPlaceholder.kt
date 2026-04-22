package com.farmconnect.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily

data class EmptyListPlaceholderUiState(
    @param:DrawableRes val image: Int = R.drawable.placeholder,
    val imageVector: ImageVector? = null,
    val text: String = "No items found."
)

class EmptyListPlaceholderPreviewParameterProvider :
    PreviewParameterProvider<EmptyListPlaceholderUiState> {
    override val values: Sequence<EmptyListPlaceholderUiState>
        get() = sequenceOf(
            EmptyListPlaceholderUiState(),
        )
}


@Composable
fun EmptyListPlaceholder(
    modifier: Modifier = Modifier,
    uiState: EmptyListPlaceholderUiState = EmptyListPlaceholderUiState(),
    actions: @Composable () -> Unit = {}
) {

    uiState.run {
        Column(
            modifier = modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            imageVector?.let {
                Image(
                    imageVector = imageVector,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Fit
                )
            } ?: run {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Fit
                )
            }


            Text(
                text = text,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = LocalFontFamily.current,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(Modifier.height(8.dp))

            actions()

        }
    }

}

@Preview
@Composable
private fun EmptyListPlaceholderPrev(
    @PreviewParameter(EmptyListPlaceholderPreviewParameterProvider::class)
    uiState: EmptyListPlaceholderUiState
) {
    FarmConnectTheme {
        EmptyListPlaceholder(
            uiState = uiState
        )
    }
}