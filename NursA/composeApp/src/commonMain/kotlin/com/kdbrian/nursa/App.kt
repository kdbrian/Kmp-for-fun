package com.kdbrian.nursa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.nursa.features.onboard.ui.screens.OnboardingScreen
import com.kdbrian.nursa.features.onboard.ui.theme.LocalOnPrimaryColor
import com.kdbrian.nursa.features.onboard.ui.theme.LocalPrimaryColor
import com.kdbrian.nursa.features.onboard.ui.theme.onPrimaryColor
import com.kdbrian.nursa.features.onboard.ui.theme.primaryColor

@Composable
@Preview
fun App() {


    MaterialTheme {
        CompositionLocalProvider(
            LocalPrimaryColor provides primaryColor,
            LocalOnPrimaryColor provides onPrimaryColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)

            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFE08DAC),
                                    Color(0xFFE08DAC).copy(.6f),
                                    Color(0xFFE08DAC).copy(.2f),
                                )
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(.45f))
                        .blur(12.dp)
                        .alpha(.3f)
                )
                OnboardingScreen()
            }
        }
    }
}