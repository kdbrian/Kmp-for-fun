package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor

@Composable
fun CategoryChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    icon: ImageVector,
    background: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = background,
        contentColor = contentColor,
        shape = RoundedCornerShape(50),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (selected) FontWeight.W400 else FontWeight.Light,
                    fontFamily = LocalFontFamily.current
                )
            )

            Icon(
                imageVector = icon,
                contentDescription = text
            )
        }
    }
}

@Preview
@Composable
private fun CategoryChipPrev() {
    FarmConnectTheme {
        CategoryChip(
            text = "Fruits",
            selected = true,
            icon = Icons.Rounded.Add,
            background = LocalPrimaryColor.current,
            contentColor = Color.White,
            onClick = {}
        )
    }
}