package com.kdbrian.nursa.features.newsapp.domain.usecases

import com.kdbrian.nursa.config.util.Resource
import com.kdbrian.nursa.features.newsapp.domain.repo.NewsRepo
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.flow

class FetchTopNewsUseCase(
    private val newsRepo: NewsRepo
) {

    fun invoke(
        date: String? = null,
        max: Int = 20,
        sourceCountry: String = "KE",
        headline: Boolean = false,
    ) = flow {
        emit(Resource.Loading())

        newsRepo
            .topNews(
                latestDate = date,
                sourceCountry = sourceCountry,
                headline = headline,
                max = max,
            )
            .onSuccess { newsResponse ->
                emit(Resource.Success(newsResponse))
            }.onFailure {
                Napier.e(it.message.toString())
                emit(Resource.Error(it.message ?: "Something went wrong"))
            }
    }
}