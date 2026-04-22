package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily

@Composable
fun UtilityCard(
    modifier: Modifier = Modifier,
    icon: @Composable Modifier.() -> Unit = {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = null
        )
    },
    title: @Composable Modifier.() -> Unit = {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = LocalFontFamily.current
            )
        )
    },
    subtitle: @Composable Modifier.() -> Unit = {
    },
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {}
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(12.dp)),
        shape = shape,
        color = color,
        contentColor = contentColor,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon(Modifier.weight(.5f))
            title(Modifier.weight(.25f))
            subtitle(Modifier.weight(.15f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityCardPrev() {
    FarmConnectTheme {
        UtilityCard()
    }

}
