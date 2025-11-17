package com.apska.domain.model

data class Weather(
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
    val windDegree: Float,

    val hourList: List<WeatherHour> = emptyList(),
    val dayList: List<WeatherDay> = emptyList()
)