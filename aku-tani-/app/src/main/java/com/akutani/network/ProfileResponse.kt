package com.akutani.network

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val message: String,
    val data: ProfileUser
)

data class ProfileUser(
    val name: String,
    val email: String,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)
