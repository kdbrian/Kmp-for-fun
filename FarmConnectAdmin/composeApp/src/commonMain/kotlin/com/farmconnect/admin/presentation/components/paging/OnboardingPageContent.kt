package com.farmconnect.core.ui.components.paging

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.theme.AppLightBlue
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    toggleImageBg : Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {

            this@Column.AnimatedVisibility(
                toggleImageBg,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(shape = CircleShape, color = AppLightBlue.copy(.65f)),
                )
            }

            // Image
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentScale = ContentScale.Fit
            )

        }

        // Title
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = LocalFontFamily.current,
                color = LocalPrimaryColor.current,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )


        // Description
        Text(
            text = page.description,
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color(0xFF757575),
                fontFamily = LocalFontFamily.current,
            ),
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingPageContentPrev() {
    FarmConnectTheme {
        OnboardingPageContent(
            page = getOnboardingPages().first()
        )
    }
}