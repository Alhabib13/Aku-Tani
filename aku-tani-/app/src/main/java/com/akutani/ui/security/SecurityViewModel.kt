package com.akutani.ui.security

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akutani.network.ChangePasswordRequest
import com.akutani.network.Api.ProfileApiClient
import kotlinx.coroutines.launch

class SecurityViewModel(
    private val context: android.content.Context
) : ViewModel() {

    var uiState = mutableStateOf(SecurityUiState())
        private set

    fun onOldPasswordChange(v: String) {
        uiState.value = uiState.value.copy(oldPassword = v)
    }

    fun onNewPasswordChange(v: String) {
        uiState.value = uiState.value.copy(newPassword = v)
    }

    fun onConfirmPasswordChange(v: String) {
        uiState.value = uiState.value.copy(confirmPassword = v)
    }

    fun changePassword() {
        val state = uiState.value

        if (
            state.oldPassword.isBlank() ||
            state.newPassword.isBlank() ||
            state.confirmPassword.isBlank()
        ) {
            uiState.value = state.copy(
                errorMessage = "Semua field wajib diisi"
            )
            return
        }

        if (state.newPassword != state.confirmPassword) {
            uiState.value = state.copy(
                errorMessage = "Konfirmasi password tidak sama"
            )
            return
        }

        viewModelScope.launch {
            uiState.value = state.copy(isLoading = true, errorMessage = null)

            try {
                val api = ProfileApiClient.create(context)

                api.changePassword(
                    ChangePasswordRequest(
                        old_password = state.oldPassword,
                        new_password = state.newPassword
                    )
                )

                uiState.value = state.copy(
                    isLoading = false,
                    successMessage = "Password berhasil diubah"
                )

            } catch (e: Exception) {
                uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = "Password lama salah atau gagal"
                )
            }
        }
    }
}
