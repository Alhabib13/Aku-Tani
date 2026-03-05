package com.akutani.ui.kalkulator_panen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.akutani.network.Api.PanenApiClient
import com.akutani.network.PanenRequest
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class KalkulatorPanenViewModel : ViewModel() {

    var uiState = mutableStateOf(KalkulatorPanenUiState())
        private set

    fun onLuasChange(v: String) {
        uiState.value = uiState.value.copy(luasLahan = v)
    }

    fun onCurahHujanChange(v: String) {
        uiState.value = uiState.value.copy(curahHujan = v)
    }

    fun onKualitasTanahChange(v: String) {
        uiState.value = uiState.value.copy(kualitasTanah = v)
    }

    fun onJumlahPupukChange(v: String) {
        uiState.value = uiState.value.copy(jumlahPupuk = v)
    }

    fun onLamaTanamChange(v: String) {
        uiState.value = uiState.value.copy(lamaTanam = v)
    }

    fun hitungPanen() {
        val state = uiState.value

        viewModelScope.launch {
            try {
                val res = PanenApiClient.api.predictPanen(
                    PanenRequest(
                        luas_lahan = state.luasLahan.toDouble(),
                        curah_hujan = state.curahHujan.toDouble(),
                        kualitas_tanah = state.kualitasTanah.toDouble(),
                        jumlah_pupuk = state.jumlahPupuk.toDouble(),
                        lama_tanam = state.lamaTanam.toDouble()
                    )
                )

                uiState.value = state.copy(
                    hasilCart = "${res.cart} ton/ha",
                    hasilRf = "${res.random_forest} ton/ha",
                    errorMessage = null
                )

            } catch (e: Exception) {
                uiState.value = state.copy(
                    errorMessage = "Gagal menghitung panen"
                )
            }
        }
    }

}
