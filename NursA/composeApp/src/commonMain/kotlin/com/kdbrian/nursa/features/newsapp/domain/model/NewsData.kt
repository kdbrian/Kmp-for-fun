package com.kdbrian.nursa.features.newsapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsData(
    @SerialName("authors")
    val authors: List<String>? = null,
    @SerialName("category")
    val category: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String? = null,
    @SerialName("language")
    val language: String? = null,
    @SerialName("publish_date")
    val publishDate: String? = null,
    @SerialName("sentiment")
    val sentiment: Double,
    @SerialName("source_country")
    val sourceCountry: String? = null,
    @SerialName("summary")
    val summary: String? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("video")
    val video: String? = null
)