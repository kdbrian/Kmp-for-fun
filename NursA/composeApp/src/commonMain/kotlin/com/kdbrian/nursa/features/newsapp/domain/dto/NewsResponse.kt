package com.kdbrian.nursa.features.newsapp.domain.dto

import com.kdbrian.nursa.features.newsapp.domain.model.NewsData
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class NewsResponse(
    @SerialName("available")
    val available: Int,
    @SerialName("news")
    val news: List<NewsData>,
    @SerialName("number")
    val number: Int,
    @SerialName("offset")
    val offset: Int
)



