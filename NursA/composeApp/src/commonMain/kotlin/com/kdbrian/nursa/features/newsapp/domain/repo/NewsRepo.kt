package com.kdbrian.nursa.features.newsapp.domain.repo

import com.kdbrian.nursa.features.newsapp.domain.dto.NewsResponse

interface NewsRepo {
    suspend fun searchNews(
        text: String,
        sourceCountry: String = "KE",
//        latestTime : String,
        categories : List<String>,
    ) : Result<NewsResponse>
}