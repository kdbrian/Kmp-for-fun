package com.farmconnect.core.ui.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.components.FarmConnectButton
import com.farmconnect.core.ui.theme.LocalFontFamily

object AlertDialogs {
    @Composable
    fun FarmConnectBasicDialog(
        modifier: Modifier = Modifier,
        title: String = LoremIpsum(2).values.joinToString(),
        imageVector: ImageVector? = null,
        painter: Painter? = null,
        alertState : Boolean = true,
        onDismiss: (Boolean) -> Unit = {}
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            modifier = modifier.fillMaxWidth().padding(8.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ){
                        imageVector?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = title
                            )
                        } ?: painter?.let {
                            Icon(
                                painter = it,
                                contentDescription = title
                            )
                        }
                    }


                    Text(
                        text = "⚠ Alert",
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = LocalFontFamily.current
                    )

                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = LocalFontFamily.current
                )

                FarmConnectButton(
                    title = "OK",
                    onClick = {},
                    color = Color.Green,
                    modifier = Modifier.align(Alignment.End)
                )

                FarmConnectButton(
                    title = "Dismiss",
                    onClick = {
                        onDismiss(!alertState)
                    },
                    color = Color.Red.copy(0.75f),
                    contentColor = Color.White,
                    modifier = Modifier.align(Alignment.End)
                )

            }
        }
    }
}