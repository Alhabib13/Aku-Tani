package com.akutani.ui.about

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

class AboutViewModel : ViewModel() {

    val uiState = mutableStateOf(AboutUiState())
}
