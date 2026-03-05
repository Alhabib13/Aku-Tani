package com.akutani.ui.pupuk

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akutani.network.Api.FuzzyApiClient
import com.akutani.network.FuzzyPupukRequest
import kotlinx.coroutines.launch

class PenggunaanPupukViewModel : ViewModel() {

    var uiState = mutableStateOf(PenggunaanPupukUiState())
        private set

    /* ================= ON CHANGE ================= */

    fun onTanamanChange(v: String) {
        uiState.value = uiState.value.copy(tanaman = v)
    }

    fun onLuasChange(v: String) {
        uiState.value = uiState.value.copy(luasInput = v)
    }

    fun onHasilChange(v: String) {
        uiState.value = uiState.value.copy(hasilInput = v)
    }

    fun onTanahChange(v: String) {
        uiState.value = uiState.value.copy(tanahInput = v)
    }

    fun onPupukChange(v: String) {
        uiState.value = uiState.value.copy(pupuk = v)
    }

    /* ================= HITUNG FUZZY ================= */

    fun hitungPupuk() {
        val state = uiState.value

        // ===== VALIDASI KOSONG =====
        if (
            state.tanaman.isBlank() ||
            state.luasInput.isBlank() ||
            state.hasilInput.isBlank() ||
            state.tanahInput.isBlank() ||
            state.pupuk.isBlank()
        ) {
            uiState.value = state.copy(
                errorMessage = "Semua field wajib diisi"
            )
            return
        }

        // ===== PARSING ANGKA =====
        val luas = state.luasInput.toDoubleOrNull()
        val hasil = state.hasilInput.toDoubleOrNull()
        val tanah = state.tanahInput.toDoubleOrNull()

        if (luas == null || hasil == null || tanah == null) {
            uiState.value = state.copy(
                errorMessage = "Luas, hasil, dan tanah harus berupa angka"
            )
            return
        }

        uiState.value = state.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val response = FuzzyApiClient.api.hitungPupuk(
                    FuzzyPupukRequest(
                        tanaman = state.tanaman,
                        luas = luas,
                        hasil = hasil,
                        tanah = tanah,
                        pupuk = state.pupuk
                    )
                )

                uiState.value = state.copy(
                    isLoading = false,
                    rekomendasi = "Rekomendasi pupuk: ${response.data.jumlah} ${response.data.satuan}"
                )

            } catch (e: Exception) {
                uiState.value = state.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}
