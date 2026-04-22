package com.kdbrian.nursa.features.onboard.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kdbrian.nursa.features.newsapp.NewsViewModel
import com.kdbrian.nursa.features.newsapp.supportedCountries
import com.kdbrian.nursa.features.onboard.ui.component.SelectionBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(modifier: Modifier = Modifier) {

    var selected by remember { mutableIntStateOf(1) }
    val items = listOf(
        Icons.Rounded.Explore to "Explore",
        Icons.Rounded.Newspaper to "News search",
        Icons.Rounded.Stars to "Nasa Search"
    )
    val newsViewModel: NewsViewModel = koinViewModel()
    val topNews = newsViewModel.newsResource.collectAsState()


    val pagerState = rememberPagerState() { items.size }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { currentPage ->

            AnimatedContent(
                targetState = currentPage
            ) {
                when (it) {
                    0 -> {

                        NewsOnboarding(
                            topNews = topNews.value,
                            query = newsViewModel.query.collectAsState().value,
                            onQueryChange = newsViewModel::updateQuery,
                            supportedCountries = supportedCountries,
                            selectedCountry = newsViewModel.sourceCountry.collectAsState().value,
                            onCountryChange = newsViewModel::updateSourceCountry
                        )
                    }
                }
            }

        }

        SelectionBar(
            items = items,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            selected = selected
        ) {
            selected = it
        }


    }
}

@Composable
@Preview
fun OnboardingScreenPreview() {
    OnboardingScreen()

}