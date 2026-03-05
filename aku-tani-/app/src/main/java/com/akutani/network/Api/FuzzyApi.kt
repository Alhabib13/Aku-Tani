package com.akutani.network.Api

import com.akutani.network.FuzzyPupukRequest
import com.akutani.network.FuzzyPupukResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FuzzyApi {

    @POST("fuzzy/pupuk")
    suspend fun hitungPupuk(
        @Body request: FuzzyPupukRequest
    ): FuzzyPupukResponse
}
