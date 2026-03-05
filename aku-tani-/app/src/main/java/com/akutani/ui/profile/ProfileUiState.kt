package com.akutani.ui.profile

data class ProfileUiState(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val showLogoutDialog: Boolean = false
)

