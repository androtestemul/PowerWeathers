package com.apska.presentation.screen.weather

import androidx.compose.runtime.Immutable
import com.apska.presentation.model.WeatherUi

@Immutable
data class WeatherScreenState(
    val status: WeatherScreenStatus = WeatherScreenStatus.Empty,
    val weatherData: WeatherUi? = null,
    val errorMessage: String? = null
)
