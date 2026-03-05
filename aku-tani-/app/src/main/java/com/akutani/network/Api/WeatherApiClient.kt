package com.akutani.network.Api

import com.akutani.network.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// ================== DATA CLASS ==================

data class WeatherMain(
    val temp: Double,
    val humidity: Int
)

data class WeatherInfo(
    val main: String,
    val description: String
)


// ---------- Forecast ----------

data class ForecastMain(
    val temp: Double
)

data class ForecastItem(
    val dt_txt: String,
    val main: ForecastMain,
    val weather: List<WeatherInfo>
)

data class ForecastResponse(
    val list: List<ForecastItem>
)

// ================== API ==================

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeatherByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<WeatherResponse>

    @GET("weather-forecast")
    suspend fun getForecastByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<ForecastResponse>
}

// ================== CLIENT ==================

object WeatherApiClient {

    private const val BASE_URL = "http://10.148.190.180:8000/api/"

    val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}
