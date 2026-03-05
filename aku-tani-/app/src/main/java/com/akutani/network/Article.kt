package com.akutani.network

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String?,

    @SerializedName("excerpt")
    val excerpt: String?,

    @SerializedName("content")
    val content: String?,
    
    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("source")
    val source: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
)
