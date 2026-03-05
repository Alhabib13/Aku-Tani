package com.akutani.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @SerializedName("name")
    val name: String,

    @SerializedName("main")
    val main: Main,

    @SerializedName("weather")
    val weather: List<Weather>
) {

    data class Main(
        @SerializedName("temp")
        val temp: Double,

        @SerializedName("humidity")
        val humidity: Int
    )

    data class Weather(
        @SerializedName("main")
        val main: String,

        @SerializedName("description")
        val description: String
    )
}
