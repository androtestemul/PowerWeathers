package com.apska.data.repository

import com.apska.data.dataSource.remote.WeatherApiService
import com.apska.domain.model.Weather
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryTest {

    private val mockResponseBody: ResponseBody = mockk<ResponseBody>(relaxed = true)
    private val mockWeatherApiService: WeatherApiService = mockk()

    private lateinit var repository: Repository

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
    fun setUp() {
        repository = Repository(mockWeatherApiService) // Простая инициализация
    }

    @Test
    fun `getWeather should emit success when response is successful and body is not null`() = runTest {
        // Given
        val successResponse = Response.success(testWeather)
        coEvery { mockWeatherApiService.getWeather() } returns successResponse

        // When
        val result = repository.getWeather().first()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(testWeather, result.getOrNull())
    }

    @Test
    fun `getWeather should emit failure when response is successful but body is null`() = runTest {
        // Given
        val successResponseWithNullBody = Response.success<Weather>(null)
        coEvery { mockWeatherApiService.getWeather() } returns successResponseWithNullBody

        // When
        val result = repository.getWeather().first()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
        assertEquals("No weather data received", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getWeather should emit failure when response is not successful`() = runTest {
        // Given
        val errorResponse = Response.error<Weather>(500, mockResponseBody)
        coEvery { mockWeatherApiService.getWeather() } returns errorResponse

        // When
        val result = repository.getWeather().first()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is retrofit2.HttpException)
        val httpException = result.exceptionOrNull() as retrofit2.HttpException
        assertEquals(500, httpException.code())
    }

    @Test
    fun `getWeather should emit failure when api call throws exception`() = runTest {
        // Given
        val networkException = RuntimeException("Network error")
        coEvery { mockWeatherApiService.getWeather() } throws networkException

        // When
        val result = repository.getWeather().first()

        // Then
        assertTrue(result.isFailure)
        assertEquals(networkException, result.exceptionOrNull())
    }

    @Test
    fun `getWeather should emit failure when http exception occurs`() = runTest {
        // Given
        val httpException = mockk<retrofit2.HttpException>(relaxed = true)
        coEvery { mockWeatherApiService.getWeather() } throws httpException

        // When
        val result = repository.getWeather().first()

        // Then
        assertTrue(result.isFailure)
        assertEquals(httpException, result.exceptionOrNull())
    }
}