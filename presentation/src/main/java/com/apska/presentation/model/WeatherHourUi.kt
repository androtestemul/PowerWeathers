package com.apska.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeatherHourUi(
    val dateTime : Long,
    val conditionText: String,
    val conditionIconUrl: String,
    val tempCurrent: String
)
