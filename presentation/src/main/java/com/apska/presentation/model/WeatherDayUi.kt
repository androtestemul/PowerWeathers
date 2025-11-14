package com.apska.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class WeatherDayUi(
    val date: Long,
    val conditionIconUrl: String,
    val tempAvg: String
)