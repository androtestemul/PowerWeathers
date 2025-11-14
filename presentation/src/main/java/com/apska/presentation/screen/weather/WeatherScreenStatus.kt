package com.apska.presentation.screen.weather

import com.apska.presentation.model.WeatherUi

sealed interface WeatherScreenStatus {
    object Loading : WeatherScreenStatus
    object Empty : WeatherScreenStatus
    data class Error(val message: String?) : WeatherScreenStatus
    data class Success(val weather: WeatherUi) : WeatherScreenStatus
}