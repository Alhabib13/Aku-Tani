package com.akutani.ui.profile

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs =
        application.getSharedPreferences("aku_tani_prefs", Context.MODE_PRIVATE)

    var uiState = mutableStateOf(ProfileUiState())
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        val loggedIn = prefs.getBoolean("is_logged_in", false)

        if (loggedIn) {
            uiState.value = ProfileUiState(
                isLoggedIn = true,
                username = prefs.getString("username", "") ?: "",
                email = prefs.getString("email", "") ?: "",
                avatarUrl = prefs.getString("avatar_url", null)
            )
        } else {
            uiState.value = ProfileUiState() // logged out state
        }
    }

    fun showLogoutDialog(show: Boolean) {
        uiState.value = uiState.value.copy(showLogoutDialog = show)
    }

    fun logout(onFinished: () -> Unit) {
        prefs.edit().clear().apply()

        uiState.value = ProfileUiState()
        onFinished()
    }
}
