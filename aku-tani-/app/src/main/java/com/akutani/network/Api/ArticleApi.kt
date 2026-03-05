package com.akutani.network.Api

import com.akutani.network.ArticleDetailResponse
import com.akutani.network.ArticleDto
import com.akutani.network.ArticleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ArticleApi {

    // GET /api/articles
    @GET("articles")
    suspend fun getArticles(): Response<ArticleResponse>

    // GET /api/articles/{id}
    @GET("articles/{id}")
    suspend fun getArticle(@Path("id") id: Int): Response<ArticleDetailResponse>

    // POST /api/articles  (multipart: title, excerpt, content, optional image)
    @Multipart
    @POST("articles")
    suspend fun createArticle(
        @Part("title") title: RequestBody,
        @Part("excerpt") excerpt: RequestBody?,
        @Part("content") content: RequestBody?,
        @Part image: MultipartBody.Part? = null
    ): Response<ArticleDto>

    // PUT /api/articles/{id}  (multipart for optional replacement image)
    @Multipart
    @PUT("articles/{id}")
    suspend fun updateArticle(
        @Path("id") id: Int,
        @Part("title") title: RequestBody?,
        @Part("excerpt") excerpt: RequestBody?,
        @Part("content") content: RequestBody?,
        @Part image: MultipartBody.Part? = null
    ): Response<ArticleDto>

    // DELETE /api/articles/{id}
    @DELETE("articles/{id}")
    suspend fun deleteArticle(@Path("id") id: Int): Response<Void>
}
