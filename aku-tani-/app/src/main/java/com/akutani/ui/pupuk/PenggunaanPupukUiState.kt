package com.akutani.ui.pupuk

data class PenggunaanPupukUiState(
    val tanaman: String = "",
    val luasInput: String = "",
    val hasilInput: String = "",
    val tanahInput: String = "",
    val pupuk: String = "",

    val rekomendasi: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
