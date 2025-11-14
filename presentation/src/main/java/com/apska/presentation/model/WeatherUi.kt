package com.apska.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeatherUi(
    val city: String,
    val region: String,
    val country: String,
    val lastUpdated: Long,
    val condition: String,
    val tempCurrent: String,
    val tempMin: String,
    val tempMax: String,
    val windSpeed: String,
    val windDirection: String,

    val hourList: List<WeatherHourUi> = emptyList(),
    val dayList: List<WeatherDayUi> = emptyList()
)
