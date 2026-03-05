package com.akutani.ui.home

import com.akutani.network.WeatherResponse

data class HomeUiState(
    val weather: WeatherResponse? = null,
    val locationName: String = "",
    val forecast: List<DailyForecast> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class DailyForecast(
    val dateLabel: String,
    val temp: Int,
    val main: String
)
