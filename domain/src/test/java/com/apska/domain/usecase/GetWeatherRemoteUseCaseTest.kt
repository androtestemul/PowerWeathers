package com.apska.domain.usecase

import com.apska.domain.model.Weather
import com.apska.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetWeatherRemoteUseCaseTest {

    private lateinit var useCase: GetWeatherUseCase
    private lateinit var weatherRepository: WeatherRepository

    private val testWeather = Weather(
        city = "Москва",
        region = "Moscow City",
        country = "Russia",
        lastUpdated = System.currentTimeMillis(),
        condition = "Переменная облачность",
        tempCurrent = "3.1",
        tempMin = "5.2",
        tempMax = "6.1",
        windSpeed = "5.4",
        windDirection = "SE",
        windDegree = 45f,
        hourList = emptyList(),
        dayList = emptyList()
    )

    @Before
    fun setup() {
        weatherRepository = mockk()
        useCase = GetWeatherRemoteUseCase(
            weatherRepository = weatherRepository
        )
    }

    @Test
    fun `getWeather should emit success result`() = runTest {
        // Given
        val expectedWeather = testWeather
        coEvery { weatherRepository.getWeather() } returns flow {
            emit(Result.success(expectedWeather))
        }

        // When
        val result = useCase.getWeather().first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedWeather, result.getOrNull())
    }

    @Test
    fun `getWeather should emit failure result on repository error`() = runTest {
        // Given
        val expectedError = Exception("Network unavailable")
        coEvery { weatherRepository.getWeather() } returns flow {
            emit(Result.failure(expectedError))
        }

        // When
        val result = useCase.getWeather().first()

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedError, result.exceptionOrNull())
    }

    @Test
    fun `getWeather should propagate repository flow`() = runTest {
        // Given
        val weatherList = listOf(
            testWeather,
            Weather(
                city = "Санкт-Петербург",
                region = "Набережная",
                country = "Russia",
                lastUpdated = System.currentTimeMillis(),
                condition = "Переменная облачность",
                tempCurrent = "3.1",
                tempMin = "5.2",
                tempMax = "6.1",
                windSpeed = "5.4",
                windDirection = "SE",
                windDegree = 45f,
                hourList = emptyList(),
                dayList = emptyList()
            )
        )
        coEvery { weatherRepository.getWeather() } returns flow {
            weatherList.forEach { weather ->
                emit(Result.success(weather))
            }
        }

        // When
        val results = useCase.getWeather().toList()

        // Then
        assertEquals(2, results.size)
        assertEquals(weatherList[0], results[0].getOrNull())
        assertEquals(weatherList[1], results[1].getOrNull())
    }
}