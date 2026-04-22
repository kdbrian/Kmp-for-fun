package com.farmconnect.core.ui.components.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.components.CircularPageIndicator
import com.farmconnect.core.ui.components.EmailPasswordAuthenticationFlowUiState
import com.farmconnect.core.ui.components.FarmConnectButton
import com.farmconnect.core.ui.components.paging.OnboardingPageContent
import com.farmconnect.core.ui.components.paging.getOnboardingPages
import com.farmconnect.core.ui.theme.AppBlue
import com.farmconnect.core.ui.theme.AppYellow
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.farmconnect.core.util.Resource
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    uiState: EmailPasswordAuthenticationFlowUiState = EmailPasswordAuthenticationFlowUiState(),
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val pages = getOnboardingPages()
        val pagerState = rememberPagerState(pageCount = { pages.size })
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            while (true) {
                delay(3_500.milliseconds)
                if (pagerState.currentPage == pagerState.pageCount.minus(1)) {
                    pagerState.scrollToPage(0)
                    delay(800)
                } else {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }

        // Pager for onboarding pages
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                toggleImageBg = pagerState.currentPage > 0
            )
        }


        // Page Indicators
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(pages.size) { index ->
                CircularPageIndicator(
                    isSelected = index == pagerState.currentPage,
                    selectedColor = LocalPrimaryColor.current,
                    unselectedColor = Color.LightGray
                )
            }
        }


        AnimatedContent(
            targetState = uiState.authenticationResource is Resource.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                contentAlignment = Alignment.Center
            ){
                when (it) {
                    true -> CircularProgressIndicator(
                        color = LocalPrimaryColor.current,
                        trackColor = Color.Transparent,
                    )

                    else -> Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        FarmConnectButton(
                            shape = RoundedCornerShape(12.dp),
                            onClick = onGetStarted,
                            color = AppYellow,
                            contentColor = Color.White,
                            title = "Get started",
                        )

                        FarmConnectButton(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                            onClick = onSignIn,
                            color = AppBlue,
                            contentColor = Color.White,
                            title = "Sign in",
                        )
                    }
                }
            }
        }


        Text(
            text = "By continuing you agree to Terms of Service and Privacy Policy.",
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = LocalFontFamily.current,
                textAlign = TextAlign.Center,
                color = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )


    }


}


@Preview(
    showBackground = true,
)
@Composable
private fun OnboardingScreenPrev() {
    FarmConnectTheme {
        OnboardingScreen()
    }
}