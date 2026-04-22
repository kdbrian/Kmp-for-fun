package com.kdbrian.nursa.features.onboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kdbrian.nursa.features.onboard.ui.theme.LocalPrimaryColor

@Composable
fun SelectionBar(
    modifier: Modifier = Modifier.Companion,
    selected: Int,
    items: List<Pair<ImageVector, String>> = emptyList(),
    primaryColor: Color = LocalPrimaryColor.current,
    onSelectItem: (Int) -> Unit = {}
) {


    Box(
        modifier = modifier
            .padding(6.dp)
            .background(
                brush = Brush.Companion.radialGradient(
                    colors = listOf(
                        primaryColor.copy(.4f),
                        Color.Companion.White.copy(.18f),
                        primaryColor.copy(.2f)
                    ),
                ),
                shape = RoundedCornerShape(50)
            )
            .dropShadow(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                shadow = Shadow(
                    color = primaryColor.copy(.2f),
                    radius = 16.dp,
                    spread = 12.dp,
                    blendMode = BlendMode.Darken,
                )
            )
            .border(
                width = 1.dp,
                color = Color.Companion.White.copy(.2f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
            )
    ) {
        Box(
            Modifier.blur(16.dp)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEachIndexed { index, item ->
                SelectionItem(
                    iconVector = item.first,
                    isSelected = index == selected,
                    title = item.second,
                    primaryColor = primaryColor,
                    onClick = { onSelectItem(index) }
                )
            }
        }
    }
}