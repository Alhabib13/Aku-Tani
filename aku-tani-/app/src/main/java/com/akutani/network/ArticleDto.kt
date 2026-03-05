package com.akutani.network

import com.google.gson.annotations.SerializedName

data class ArticleDto(
    val id: Int,

    val title: String? = null,
    val excerpt: String? = null,

    // ✅ ISI ARTIKEL DARI BACKEND (content)
    @SerializedName("content")
    val body: String? = null,

    @SerializedName("image_url")
    val imageUrl: String? = null,

    val source: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)
