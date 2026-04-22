package com.farmconnect.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.LocalFontFamily

@Composable
fun SettingsRowItem(
    modifier: Modifier = Modifier,
    title: AnnotatedString = buildAnnotatedString { append(LoremIpsum(2).values.joinToString()) },
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(2.dp),
    color: Color = MaterialTheme.colorScheme.surface,
    startIcon: ImageVector? = null,
    centerTitle: Boolean = false,
    startIconDrawable: Painter? = null,
    endIcon: (@Composable () -> Unit)? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {

    Surface(
        modifier = modifier,
        color = color,
        onClick = {
            onClick?.invoke()
        },
        contentColor = contentColor,
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                startIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = title.text
                    )
                } ?: startIconDrawable?.let {
                    Image(
                        painter = it,
                        contentDescription = title.text,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = title,
                    modifier = Modifier
                        .then(
                            if (centerTitle)
                                Modifier.fillMaxWidth()
                            else
                                Modifier
                        ),
                    textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = LocalFontFamily.current
                )
            }

            endIcon?.invoke()
        }
    }

}

@Preview
@Composable
private fun SettingsRowItemPrev() {
    SettingsRowItem()
}