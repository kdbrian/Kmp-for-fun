package com.kdbrian.nursa.features.newsapp.data.impl

import NursA.composeApp.BuildConfig
import com.kdbrian.nursa.config.Urls
import com.kdbrian.nursa.config.util.safeApiCall
import com.kdbrian.nursa.features.newsapp.domain.dto.NewsResponse
import com.kdbrian.nursa.features.newsapp.domain.dto.TopNewsResponse
import com.kdbrian.nursa.features.newsapp.domain.repo.NewsRepo
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class NewsRepoImpl(
    private val client: HttpClient
) : NewsRepo {
    override suspend fun searchNews(
        text: String,
        language: String,
        sourceCountry: String,
//        latestTime: String,
        categories: List<String>
    ): Result<NewsResponse> = safeApiCall {
        val response = client.get(Urls.newsBaseUrl.plus("search-news")) {
            parameter("text", text)
            parameter("source-country", sourceCountry)
//            parameter("latest-publish-date", latestTime)
            if (categories.isNotEmpty())
                parameter("categories", categories)
            parameter("api-key", BuildConfig.newsApiKey)
            parameter("language", language)
            parameter("number", 70)
        }.bodyAsText()
        Napier.d("Response: $response")
        Json.decodeFromString(response)

    }

    override suspend fun topNews(
        sourceCountry: String,
        language: String,
        headline: Boolean,
        max: Int,
        latestDate: String?
    ): Result<TopNewsResponse> = safeApiCall {
        client.get(Urls.newsBaseUrl.plus("top-news")) {
            parameter("source-country", sourceCountry)
            latestDate?.let {
                parameter("date", latestDate)
            }
            parameter("max-news-per-cluster", max)
            parameter("api-key", BuildConfig.newsApiKey)
            parameter("language", language)
        }.body()
    }
}