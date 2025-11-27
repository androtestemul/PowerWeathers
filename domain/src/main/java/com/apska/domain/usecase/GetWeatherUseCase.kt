package com.apska.domain.usecase

import com.apska.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface GetWeatherUseCase {
    fun getWeather(): Flow<Result<Weather>>
}