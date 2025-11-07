package com.apska.presentation.screen.weather

import com.apska.domain.model.Weather

sealed interface WeatherScreenStatus {
    object Loading : WeatherScreenStatus
    object Empty : WeatherScreenStatus
    data class Error(val message: String?) : WeatherScreenStatus
    data class Success(val weather: Weather) : WeatherScreenStatus
}