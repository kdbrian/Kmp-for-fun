package com.kdbrian.nursa.features.onboard.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.toFontFamily
import nursa.composeapp.generated.resources.Res
import nursa.composeapp.generated.resources.geom
import nursa.composeapp.generated.resources.platypi
import org.jetbrains.compose.resources.Font


val Geom: FontFamily
    @Composable
    get() = Font(
        Res.font.geom
    ).toFontFamily()

val PlatyPi: FontFamily
    @Composable
    get() = Font(
        Res.font.platypi
    ).toFontFamily()