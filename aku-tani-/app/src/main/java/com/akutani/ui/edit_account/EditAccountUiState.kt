package com.akutani.ui.edit_account

import android.net.Uri

data class EditAccountUiState(
    val name: String = "",
    val email: String = "",
    val avatarUri: Uri? = null,
    val isLoading: Boolean = false
)
