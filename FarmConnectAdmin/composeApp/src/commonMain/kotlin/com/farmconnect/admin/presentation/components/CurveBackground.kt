package com.farmconnect.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.farmconnect.core.ui.theme.AppYellow
import com.farmconnect.core.ui.theme.FarmConnectTheme

fun Modifier.curveBackground(
    topColor: Color = AppYellow,
    bottomColor: Color = Color(0xFFF15946),
    curveDepth: Float = 120f,
    splitRatio: Float = 0.25f // 1/4 of screen height
): Modifier = this.then(
    Modifier.drawBehind {
        val w = size.width
        val h = size.height
        val split = h * splitRatio

        // Draw top section color
        drawRect(
            color = topColor,
            size = androidx.compose.ui.geometry.Size(w, split)
        )

        // Draw bottom section color
        drawRect(
            color = bottomColor,
            topLeft = Offset(0f, split),
            size = androidx.compose.ui.geometry.Size(w, h - split)
        )

        // Draw the curve at the split
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, split)
            quadraticTo(
                w / 2f,
                split + curveDepth,
                w, split
            )
            lineTo(w, split)
            close()
        }

        drawPath(
            path = path,
            color = topColor
        )
    }
)

@Preview
@Composable
private fun CurveBackgroundPrev() {
    FarmConnectTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .curveBackground()
        )
    }
}