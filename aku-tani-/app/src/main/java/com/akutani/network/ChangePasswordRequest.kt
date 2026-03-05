package com.akutani.network

data class ChangePasswordRequest(
    val old_password: String,
    val new_password: String
)
