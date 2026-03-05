package com.akutani.ui.article.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akutani.network.Api.ArticleApiClient
import kotlinx.coroutines.launch

class ArticleDetailViewModel : ViewModel() {

    var uiState = mutableStateOf(ArticleDetailUiState())
        private set

    fun loadArticleById(id: Int) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)

            try {
                val resp = ArticleApiClient.api.getArticle(id)

                if (resp.isSuccessful) {
                    val article = resp.body()?.data
                    android.util.Log.d("ARTICLE_DEBUG", article.toString())

                    uiState.value = uiState.value.copy(
                        article = article,
                        isLoading = false
                    )
                }
                else {
                    uiState.value = uiState.value.copy(
                        error = "Artikel tidak ditemukan",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    error = "Gagal memuat artikel",
                    isLoading = false
                )
            }
        }
    }
}
