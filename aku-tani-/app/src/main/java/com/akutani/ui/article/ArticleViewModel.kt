package com.akutani.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akutani.network.Api.ArticleApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState

    init {
        fetchArticles()
    }

    fun fetchArticles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val resp = ArticleApiClient.api.getArticles()
                if (resp.isSuccessful) {
                    _uiState.value = ArticleUiState(
                        articles = resp.body()?.data ?: emptyList(),
                        isLoading = false
                    )
                }
                     else {
                    _uiState.value = ArticleUiState(
                        isLoading = false,
                        error = "Gagal memuat artikel"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ArticleUiState(
                    isLoading = false,
                    error = "Koneksi gagal"
                )
            }
        }
    }
}
