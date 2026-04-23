@file:OptIn(FlowPreview::class)

package com.kdbrian.nursa.features.newsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.nursa.features.newsapp.domain.model.NewsData
import com.kdbrian.nursa.features.newsapp.domain.repo.NewsRepo
import com.kdbrian.nursa.features.newsapp.domain.usecases.FetchTopNewsUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

val supportedCountries = listOf(
        "Kenya" to "ken",
        "United States Of America" to "us",
        "United Arab Emirates" to "ae",
        "Iran" to "ir",
)

class NewsViewModel(
    private val newsRepo: NewsRepo,
    private val fetchTopNewsUseCase: FetchTopNewsUseCase
) : ViewModel() {

    private val _newsResource = MutableStateFlow<List<NewsData>>(emptyList())
    val newsResource = _newsResource.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _sourceCountry = MutableStateFlow(0)
    val sourceCountry = _sourceCountry.asStateFlow()

    private val _latestTime = MutableStateFlow(Clock.System.now().toString().substring(0, 10))
    val latestTime = _latestTime.asStateFlow()

    private val _categories = MutableStateFlow(emptyList<String>())
    val categories = _categories.asStateFlow()

    private val _newsDate = MutableStateFlow(Clock.System.now().toString().substring(0, 10))
    val newsDate = _newsDate.asStateFlow()

    init {
        viewModelScope.launch {

            _query
                .filter { it.isNotEmpty() && it.length in 4..35 }
//                .drop(1)
                .debounce(1_200)
                .onEach { qVal ->

                    Napier.d("Query: $qVal")

                    newsRepo.searchNews(
                        text = qVal,
                        sourceCountry = supportedCountries[_sourceCountry.value].second,
                        categories = _categories.value
                    ).onSuccess {
                        val newsDataSorted = it.news.sortedBy { it.image?.isNotEmpty() == true }
                        _newsResource.value = newsDataSorted + _newsResource.value
                    }
                }
                .launchIn(viewModelScope)

            combine(
                _sourceCountry,
                _latestTime,
                _categories
            ) { sourceCountry, latestTime, categories ->
                Triple(sourceCountry, latestTime, categories)
            }.debounce(800)
                .onEach { (sourceCountry, latestTime, categories) ->
                    fetchTopNewsUseCase.invoke(
                        date = latestTime,
                        sourceCountry = supportedCountries[sourceCountry].second,
                        headline = true,
                    ).collectLatest {
                        it.data?.topNews?.flatMap { it.news }?.let { news ->
                            _newsResource.update { newsData ->
                                news + newsData
                            }
                        }
                    }
                }
                .launchIn(viewModelScope)

        }
    }

    fun  updateSourceCountry(idx: Int) {
        _sourceCountry.value = idx
    }

    fun updateLatestTime(date: String) {
        _latestTime.value = date
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

}