package com.akutani.ui.article

import com.akutani.network.ArticleDto

data class ArticleUiState(
    val articles: List<ArticleDto> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
