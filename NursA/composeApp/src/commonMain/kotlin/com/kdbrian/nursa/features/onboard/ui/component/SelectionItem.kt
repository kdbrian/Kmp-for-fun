package com.kdbrian.nursa.features.onboard.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kdbrian.nursa.features.onboard.ui.theme.Geom
import com.kdbrian.nursa.features.onboard.ui.theme.LocalOnPrimaryColor
import com.kdbrian.nursa.features.onboard.ui.theme.LocalPrimaryColor
import com.kdbrian.nursa.features.onboard.ui.theme.PlatyPi

@Composable
fun SelectionItem(
    isSelected: Boolean,
    iconVector : ImageVector,
    title: String = LoremIpsum(2).values.joinToString(),
    shape: Shape = CircleShape,
    primaryColor: Color = LocalPrimaryColor.current,
    onPrimaryColor: Color = LocalOnPrimaryColor.current,
    shadow: Dp = 4.dp,
    onClick: () -> Unit = {}
) {
    Surface(
        color = animateColorAsState(
            if (isSelected)
                primaryColor
            else
                primaryColor.copy(.8f)
        ).value,
        shadowElevation = shadow,
        onClick = onClick,
        shape = shape
    ) {
        Row(
            modifier = Modifier.Companion.padding(8.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = if (!isSelected) Arrangement.Center else Arrangement.spacedBy(
                6.dp
            )
        ) {
            AnimatedVisibility(isSelected, enter = fadeIn(), exit = fadeOut()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = onPrimaryColor,
                    fontFamily = Geom
                )
            }
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = animateColorAsState(
                    if (isSelected) onPrimaryColor else Color.LightGray
                ).value,
                modifier = Modifier.Companion.size(35.dp)
            )
        }
    }
}