package com.kdbrian.nursa.features.newsapp.domain.repo

import com.kdbrian.nursa.features.newsapp.domain.dto.NewsResponse
import com.kdbrian.nursa.features.newsapp.domain.dto.TopNewsResponse

interface NewsRepo {
    suspend fun searchNews(
        text: String,
        language: String = "en",
        sourceCountry: String = "KE",
//        latestTime : String,
        categories : List<String>,
    ) : Result<NewsResponse>

    suspend fun topNews(
        sourceCountry: String = "KE",
        language: String = "en",
        headline: Boolean = false,
        max:Int=70,
        latestDate : String?,
    ) : Result<TopNewsResponse>
}