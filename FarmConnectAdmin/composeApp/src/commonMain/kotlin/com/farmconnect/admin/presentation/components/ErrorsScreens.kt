package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.LocalFontFamily

object ErrorsScreens {

    @Composable
    fun DefaultErrorScreen(
        modifier: Modifier = Modifier,
        message: String = LoremIpsum(2).values.joinToString(),
        action: String = LoremIpsum(2).values.joinToString(),
        onAction: (() -> Unit)? = null
    ) {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {

                Text(
                    text = message,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = LocalFontFamily.current
                )

                TextButton(onClick = {
                    onAction?.invoke()
                }) {
                    Text(
                        text = action,
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = LocalFontFamily.current
                    )
                }
            }

        }


    }
}