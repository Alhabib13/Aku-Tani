package com.akutani.network

data class FuzzyPupukResponse(
    val status: Boolean,
    val data: FuzzyPupukData
)

data class FuzzyPupukData(
    val jumlah: Double,
    val satuan: String
)
