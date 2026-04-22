@file:OptIn(FlowPreview::class)

package com.kdbrian.nursa.features.newsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdbrian.nursa.config.util.Resource
import com.kdbrian.nursa.features.newsapp.domain.dto.NewsResponse
import com.kdbrian.nursa.features.newsapp.domain.repo.NewsRepo
import io.github.aakira.napier.Napier
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepo: NewsRepo
) : ViewModel() {

    private val _newsResource = MutableStateFlow<Resource<NewsResponse>>(Resource.Idle())
    val newsResource = _newsResource.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _sourceCountry = MutableStateFlow("")
    val sourceCountry = _sourceCountry.asStateFlow()

    private val _latestTime = MutableStateFlow("")
    val latestTime = _latestTime.asStateFlow()

    private val _categories = MutableStateFlow(emptyList<String>())
    val categories = _categories.asStateFlow()

    init {
        _query
            .filter { it.isNotEmpty() && it.length in 6..35 }
            .drop(1)
            .debounce(1_800)
            .distinctUntilChanged()
            .onEach { qVal ->

                Napier.d("Query: $qVal")

                _newsResource.value = Resource.Loading()
                newsRepo.searchNews(
                    text = qVal,
                    sourceCountry = _sourceCountry.value,
//                    latestTime = _latestTime.value,
                    categories = _categories.value
                ).onSuccess {
                    val newsDataSorted = it.news.sortedBy { it.image?.isNotEmpty() == true }
                    _newsResource.value = Resource.Success(
                        it.copy(
                            news = newsDataSorted
                        )
                    )
                }.onFailure {
                    _newsResource.value = Resource.Error(it.message.toString())
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

}