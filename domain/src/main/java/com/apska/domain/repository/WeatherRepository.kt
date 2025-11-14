package com.apska.domain.repository

import com.apska.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeather() : Flow<Result<Weather>>
}