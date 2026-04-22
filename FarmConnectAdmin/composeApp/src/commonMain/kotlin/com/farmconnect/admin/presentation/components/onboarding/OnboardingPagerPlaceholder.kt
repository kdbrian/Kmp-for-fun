package com.farmconnect.core.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmconnect.core.ui.R
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalFontFamily
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
fun OnboardingPagerPlaceholder(
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { 8 },
    pagerContent: @Composable PagerScope.(Int) -> Unit = {

    },
    userScrollEnabled: Boolean = true
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        userScrollEnabled = userScrollEnabled
    ) { page ->
        key(page) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12 .dp),
                contentAlignment = Alignment.Center
            ) {
                pagerContent(page)
            }
        }
    }
}


@Preview
@Composable
private fun OnboardingPagerPlaceholderPrev() {
    FarmConnectTheme {
        val demoOnboardPagerState = rememberPagerState { 3 }
        val coroutineScope = rememberCoroutineScope()

        OnboardingPagerPlaceholder(
            pagerState = demoOnboardPagerState,
            pagerContent = {
                when (demoOnboardPagerState.currentPage) {
                    0 -> {
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(300.dp)
                                .rotate(22f),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF20FC8F)
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.grocery),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(300.dp)
                                .rotate(-(68).toFloat()),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF79256)
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.grocery),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Card(
                            modifier = Modifier
                                .widthIn(min = 200.dp)
                                .padding(
                                    horizontal = 32.dp,
                                ),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 12.dp
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.grocery),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        SpanStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ) {
                                        append("Your Favourite Grocery Store\n")
                                    }

                                    withStyle(
                                        SpanStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ) {
                                        withStyle(
                                            SpanStyle(
                                                fontSize = 20.sp,
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color(0xFF20FC8F),
                                                        Color(0xFFF79256)
                                                    )
                                                ),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        ) {
                                            append("O")
                                        }
                                        append("n ")
                                        withStyle(
                                            SpanStyle(
                                                fontSize = 20.sp,
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color(0xFF20FC8F),
                                                        Color(0xFFF79256)
                                                    )
                                                ),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        ) {
                                            append("T")
                                        }
                                        append("he")
                                        withStyle(
                                            SpanStyle(
                                                fontSize = 20.sp,
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color(0xFF20FC8F),
                                                        Color(0xFFF79256)
                                                    )
                                                ),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        ) {
                                            append(" G")
                                        }
                                        append("0")
                                    }
                                },
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.titleLarge,
                                fontFamily = LocalFontFamily.current,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp, horizontal = 9.dp),
                            )

                            FarmConnectButton(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                                title = "Get Started",
                                onClick = {
                                    coroutineScope.launch {
                                        demoOnboardPagerState.animateScrollToPage(
                                            1, animationSpec = spring(
                                                dampingRatio = 0.5f,
                                                stiffness = 100f
                                            )
                                        )
                                    }
                                }
                            )

                            FarmConnectButton(
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 2.dp
                                ),
                                title = "Sign Up",
                                onClick = {
                                    coroutineScope.launch {
                                        demoOnboardPagerState.animateScrollToPage(
                                            2, animationSpec = spring(
                                                dampingRatio = 0.5f,
                                                stiffness = 100f
                                            )
                                        )
                                    }
                                }
                            )
                        }

                    }

                    1 -> {
                        Text(
                            text = "\uD83E\uDD17",
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier
                                .clickable {
                                    coroutineScope.launch {
                                        demoOnboardPagerState.animateScrollToPage(
                                            2, animationSpec = spring(
                                                dampingRatio = 0.5f,
                                                stiffness = 100f
                                            )
                                        )
                                    }
                                }

                        )
                    }

                    2 ->
                        Text(
                            text = "🤪", style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier
                                .clickable {
                                    coroutineScope.launch {
                                        demoOnboardPagerState.animateScrollToPage(
                                            0, animationSpec = spring(
                                                dampingRatio = 0.5f,
                                                stiffness = 100f
                                            )
                                        )
                                    }
                                }
                        )

                }
            }
        )
    }
}