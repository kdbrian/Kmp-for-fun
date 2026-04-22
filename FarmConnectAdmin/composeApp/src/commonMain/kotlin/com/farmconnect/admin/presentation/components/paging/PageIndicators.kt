package com.farmconnect.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularPageIndicator(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    selectedColor: Color = Color.Black,
    unselectedColor: Color = Color.LightGray
) {
    Box(
        modifier = modifier
            .size(
                width = animateDpAsState(if (isSelected) 24.dp else 12.dp).value,
                height = 8.dp
            )
            .clip(CircleShape)
            .background(animateColorAsState(if (isSelected) selectedColor else unselectedColor).value)
    )
}