package com.apska.domain.usecase

import com.apska.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetWeatherRemoteUseCase(
    private val weatherRepository: WeatherRepository
) : GetWeatherUseCase {
    override fun getWeather() = weatherRepository.getWeather()
}