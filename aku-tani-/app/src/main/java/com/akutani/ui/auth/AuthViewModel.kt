package com.akutani.ui.auth

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.akutani.network.Api.ApiClient
import com.akutani.network.Api.LoginRequest
import com.akutani.network.Api.RegisterRequest
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    var uiState = mutableStateOf(AuthUiState())
        private set

    private val prefs =
        app.getSharedPreferences("aku_tani_prefs", Context.MODE_PRIVATE)

    /* ================= LOGIN ================= */

    fun login(email: String, password: String) {
        uiState.value = AuthUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val res = ApiClient.authApi.login(
                    LoginRequest(email, password)
                )

                if (res.isSuccessful && res.body() != null) {
                    val body = res.body()!!

                    prefs.edit()
                        .putBoolean("is_logged_in", true)
                        .putString("username", body.user.name)
                        .putString("email", body.user.email)
                        .putString("token", body.token)
                        .putString("avatar_url", body.user.avatarUrl)
                        .apply()


                    // ✅ TRIGGER SUKSES
                    uiState.value = AuthUiState(isSuccess = true)

                } else {
                    uiState.value = AuthUiState(
                        errorMessage = "Email atau password salah"
                    )
                }

            } catch (e: Exception) {
                uiState.value = AuthUiState(
                    errorMessage = "Gagal terhubung ke server"
                )
            }
        }
    }

    /* ================= REGISTER ================= */

    fun register(
        username: String,
        email: String,
        password: String,
        confirm: String
    ) {
        uiState.value = AuthUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val res = ApiClient.authApi.register(
                    RegisterRequest(username, email, password, confirm)
                )

                if (res.isSuccessful) {
                    uiState.value = AuthUiState(isSuccess = true)
                } else {
                    uiState.value = AuthUiState(
                        errorMessage = "Register gagal"
                    )
                }

            } catch (e: Exception) {
                uiState.value = AuthUiState(
                    errorMessage = "Gagal terhubung ke server"
                )
            }
        }
    }

    /* ================= RESET STATE (OPSIONAL) ================= */

    fun resetState() {
        uiState.value = AuthUiState()
    }
}
