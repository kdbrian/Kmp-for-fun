package com.farmconnect.core.ui.theme

import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val AppOrange = Color(0xFFF15946)
val LocalPrimaryColor = staticCompositionLocalOf {
    AppOrange
}
val AppBlue = Color(0xFF2E86AB)
val AppLightBlue = Color(0xFF53B3CB)
val AppYellow = Color(0xFFF9C22E)
val Processed = Color(0xFF2196F3)   // Blue 500
val Completed = Color(0xFF00897B)   // Teal 600
val Cancelled = Color(0xFF9E9E9E)   // Grey 500
val Pending   = Color(0xFFFFB300)   // Amber 600
val Failed    = Color(0xFFE53935)   // Red 600
val Success   = Color(0xFF43A047)   // Green 600
val Staged    = Color(0xFF8E24AA)   // Purple 500
val Unknown   = Color(0xFF78909C)   // Blue Grey 400


val LocalIconButtonColors = staticCompositionLocalOf {
    IconButtonColors(
        containerColor = AppOrange,
        contentColor = Color.White,
        disabledContainerColor = AppOrange.copy(.55f),
        disabledContentColor = Color.LightGray,
    )
}
