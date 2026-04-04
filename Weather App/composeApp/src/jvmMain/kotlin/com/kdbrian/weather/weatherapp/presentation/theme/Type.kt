package com.kdbrian.weather.weatherapp.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import org.jetbrains.compose.resources.Font
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.lugrasimo_regular

val lugrasimo_regular
    @Composable get() =
        Font(
            Res.font.lugrasimo_regular,
            weight = FontWeight.Normal,
        ).toFontFamily()

