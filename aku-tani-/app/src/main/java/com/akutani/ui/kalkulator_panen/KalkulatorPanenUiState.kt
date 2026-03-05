package com.akutani.ui.kalkulator_panen

data class KalkulatorPanenUiState(
    val luasLahan: String = "",
    val curahHujan: String = "",
    val kualitasTanah: String = "",
    val jumlahPupuk: String = "",
    val lamaTanam: String = "",

    val hasilCart: String? = null,
    val hasilRf: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
