package com.akutani.network.Api

import com.akutani.network.ChangePasswordRequest
import com.akutani.network.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ProfileApi {

    @Multipart
    @POST("profile")
    suspend fun updateProfile(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part avatar: MultipartBody.Part?
    ): ProfileResponse

    @POST("change-password")
    suspend fun changePassword(
        @Body body: ChangePasswordRequest
    ): Response<Unit>
}
