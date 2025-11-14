package com.apska.domain.model

data class WeatherHour(
    val dateTime : Long,
    val conditionText: String,
    val conditionIconUrl: String,
    val tempCurrent: String
)
