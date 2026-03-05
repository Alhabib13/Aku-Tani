package com.akutani.ui.info_harga

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

class InfoHargaViewModel : ViewModel() {

    var uiState = mutableStateOf(
        InfoHargaUiState(
            items = listOf(
                HargaItemUi("Jagung", "Rp 5.000 / kg"),
                HargaItemUi("Padi", "Rp 6.500 / kg"),
                HargaItemUi("Kacang", "Rp 12.000 / kg"),
                HargaItemUi("Bawang Merah", "Rp 28.000 / kg"),
                HargaItemUi("Cabai", "Rp 45.000 / kg")
            )
        )
    )
        private set

    fun onItemClick(index: Int) {
        uiState.value = uiState.value.copy(
            items = uiState.value.items.mapIndexed { i, item ->
                if (i == index) {
                    item.copy(isExpanded = !item.isExpanded)
                } else {
                    item.copy(isExpanded = false)
                }
            }
        )
    }
}
