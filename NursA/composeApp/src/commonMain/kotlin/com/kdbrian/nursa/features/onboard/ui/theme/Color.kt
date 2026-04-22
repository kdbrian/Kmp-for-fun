package com.kdbrian.nursa.features.onboard.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val primaryColor = Color(0xFFE08DAC)
val onPrimaryColor = Color.White

val LocalPrimaryColor = staticCompositionLocalOf {
    primaryColor
}
val LocalOnPrimaryColor = staticCompositionLocalOf {
    onPrimaryColor
}