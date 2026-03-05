package com.akutani.ui.article.detail

import com.akutani.network.ArticleDto

data class ArticleDetailUiState(
    val article: ArticleDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
