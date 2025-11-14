package com.apska.domain.usecase

import com.apska.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetWeatherRemoteUseCase(
    private val weatherRepository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher
) : GetWeatherUseCase {
    override suspend fun getWeather() = withContext(dispatcher) {
        weatherRepository.getWeather()
    }
}