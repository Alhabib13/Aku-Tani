package com.akutani.ui.info_harga

data class HargaItemUi(
    val nama: String,
    val harga: String,
    val isExpanded: Boolean = false
)

data class InfoHargaUiState(
    val items: List<HargaItemUi> = emptyList()
)
