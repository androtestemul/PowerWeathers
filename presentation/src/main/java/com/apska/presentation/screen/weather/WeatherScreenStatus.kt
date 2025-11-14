package com.apska.presentation.screen.weather

import androidx.compose.runtime.Immutable

@Immutable
sealed class WeatherScreenStatus {
    object Loading : WeatherScreenStatus()
    object Empty : WeatherScreenStatus()
    object Error : WeatherScreenStatus()
    object Success : WeatherScreenStatus()
}