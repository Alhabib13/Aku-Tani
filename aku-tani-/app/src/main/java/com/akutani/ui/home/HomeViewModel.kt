package com.akutani.ui.home

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.akutani.network.Api.WeatherApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var homeUiState = mutableStateOf(HomeUiState())
        private set

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(application)

    /* ================= RATE LIMIT GUARD ================= */

    private var hasFetched = false
    private var lastFetchTime = 0L
    private val CACHE_DURATION = 10 * 60 * 1000

    /* ================= ENTRY POINT ================= */

    fun fetchWeatherWithLocation() {
        val now = System.currentTimeMillis()

        if (hasFetched && now - lastFetchTime < CACHE_DURATION) {
            Log.d("HOME_VM", "Skip fetch (cache active)")
            return
        }

        hasFetched = true
        lastFetchTime = now

        val context = getApplication<Application>()

        val hasPermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            useDefaultLocation()
            return
        }

        homeUiState.value = homeUiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    fetchWeather(lat, lon)
                    updateLocationName(lat, lon)
                } else {
                    useDefaultLocation()
                }
            }
            .addOnFailureListener {
                Log.e("HOME_VM", "Location failed", it)
                useDefaultLocation()
            }
    }

    private fun useDefaultLocation() {
        homeUiState.value = homeUiState.value.copy(
            locationName = "Nganjuk, Jawa Timur",
            isLoading = true
        )
        fetchWeather(-7.6056, 111.9016)
    }

    /* ================= GEOCODER ================= */

    private fun updateLocationName(lat: Double, lon: Double) {
        if (!Geocoder.isPresent()) return

        try {
            val geocoder = Geocoder(getApplication(), Locale("id", "ID"))
            val address = geocoder.getFromLocation(lat, lon, 1)?.firstOrNull()

            if (address == null) {
                homeUiState.value =
                    homeUiState.value.copy(locationName = "Lokasi kamu")
                return
            }

            val desa = address.subLocality ?: address.featureName
            val kecamatan = address.subAdminArea
            val kabupaten = address.locality ?: address.adminArea

            val locationName = listOfNotNull(
                desa,
                kecamatan,
                kabupaten
            ).distinct().joinToString(", ")

            homeUiState.value = homeUiState.value.copy(
                locationName = locationName.ifBlank { "Lokasi kamu" }
            )

        } catch (e: Exception) {
            Log.e("HOME_VM", "Geocoder error", e)
            homeUiState.value =
                homeUiState.value.copy(locationName = "Lokasi kamu")
        }
    }

    /* ================= WEATHER ================= */

    private fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val current =
                    WeatherApiClient.api.getCurrentWeatherByCoord(lat, lon)

                val forecast =
                    WeatherApiClient.api.getForecastByCoord(lat, lon)

                val daily = forecast.body()?.list
                    ?.groupBy { it.dt_txt.substring(0, 10) }
                    ?.map { (_, items) ->
                        val first = items.first()
                        DailyForecast(
                            dateLabel = formatDay(first.dt_txt),
                            temp = first.main.temp.toInt(),
                            main = first.weather.firstOrNull()?.main ?: "Clouds"
                        )
                    }
                    ?.take(5)
                    ?: emptyList()

                homeUiState.value = homeUiState.value.copy(
                    weather = current.body(),
                    forecast = daily,
                    isLoading = false
                )

                Log.d(
                    "HOME_WEATHER",
                    "TEMP=${current.body()?.main?.temp}"
                )

            } catch (e: Exception) {
                Log.e("HOME_VM", "Weather error", e)
                homeUiState.value = homeUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal memuat cuaca"
                )
            }
        }
    }

    /* ================= UTIL ================= */

    private fun formatDay(dtTxt: String?): String {
        if (dtTxt.isNullOrBlank()) return "--"

        return try {
            val input = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val output = SimpleDateFormat("EEEE", Locale("id", "ID"))
            val date = input.parse(dtTxt)
            output.format(date ?: Date())
        } catch (e: Exception) {
            "--"
        }
    }
}
