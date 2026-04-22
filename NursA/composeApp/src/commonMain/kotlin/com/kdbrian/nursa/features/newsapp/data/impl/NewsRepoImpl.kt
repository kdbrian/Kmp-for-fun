package com.kdbrian.nursa.features.newsapp.data.impl

import NursA.composeApp.BuildConfig
import com.kdbrian.nursa.config.Urls
import com.kdbrian.nursa.config.util.safeApiCall
import com.kdbrian.nursa.features.newsapp.domain.dto.NewsResponse
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
        sourceCountry: String,
//        latestTime: String,
        categories: List<String>
    ): Result<NewsResponse> = safeApiCall {
        val response =         client.get(Urls.newsBaseUrl.plus("search-news")) {
            parameter("text", text)
            parameter("source-country", sourceCountry)
//            parameter("latest-publish-date", latestTime)
            if (categories.isNotEmpty())
                parameter("categories", categories)
            parameter("api-key", BuildConfig.newsApiKey)
            parameter("number", 70)
        }

        Napier.d("Resp ${response.bodyAsText()}")
//        Json.decodeFromString(response)
        response.body()

    }
}