package com.akutani.network.Api

import com.akutani.network.PanenRequest
import com.akutani.network.PanenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PanenApi {

    @POST("panen/predict")
    suspend fun predictPanen(
        @Body request: PanenRequest
    ): PanenResponse
}