package com.kdbrian.nursa.features.newsapp.domain.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopNewsResponse(
    @SerialName("country")
    val country: String,
    @SerialName("language")
    val language: String,
    @SerialName("top_news")
    val topNews: List<TopNew>
)