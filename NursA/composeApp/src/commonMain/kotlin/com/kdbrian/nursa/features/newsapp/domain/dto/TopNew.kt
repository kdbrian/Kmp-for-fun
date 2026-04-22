package com.kdbrian.nursa.features.newsapp.domain.dto


import com.kdbrian.nursa.features.newsapp.domain.model.NewsData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopNew(
    @SerialName("news")
    val news: List<NewsData>
)