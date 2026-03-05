package com.akutani.ui.edit_account

import android.app.Application
import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.akutani.network.ProfileRepository
import kotlinx.coroutines.launch

class EditAccountViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repo = ProfileRepository(application)

    var uiState = mutableStateOf(EditAccountUiState())
        private set

    fun setInitialData(name: String, email: String) {
        uiState.value = uiState.value.copy(
            name = name,
            email = email
        )
    }

    fun onNameChange(v: String) {
        uiState.value = uiState.value.copy(name = v)
    }

    fun onEmailChange(v: String) {
        uiState.value = uiState.value.copy(email = v)
    }

    fun onAvatarChange(uri: Uri?) {
        uiState.value = uiState.value.copy(avatarUri = uri)
    }

    fun saveProfile(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val state = uiState.value

        if (state.name.isBlank()) {
            onError("Nama wajib diisi")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            onError("Email tidak valid")
            return
        }

        viewModelScope.launch {
            uiState.value = state.copy(isLoading = true)
            try {
                repo.updateProfile(
                    name = state.name,
                    email = state.email,
                    avatarUri = state.avatarUri
                )
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Terjadi kesalahan")
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }
}
