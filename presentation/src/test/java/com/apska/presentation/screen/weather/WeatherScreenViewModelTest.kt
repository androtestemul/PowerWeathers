package com.apska.presentation.screen.weather

import app.cash.turbine.test
import com.apska.domain.model.Weather
import com.apska.domain.usecase.GetWeatherUseCase
import com.apska.presentation.mapper.toUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class WeatherScreenViewModelTest {

    private val getWeatherUseCase: GetWeatherUseCase = mockk()

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

    private fun createViewModel(weatherResult: Result<Weather>): WeatherScreenViewModel {
        coEvery { getWeatherUseCase.getWeather() } returns flowOf(weatherResult)
        return WeatherScreenViewModel(getWeatherUseCase)
    }

    private fun createViewModelWithDelay(weatherResult: Result<Weather>): WeatherScreenViewModel {
        coEvery { getWeatherUseCase.getWeather() } returns flow {
            delay(100)
            emit(weatherResult)
        }
        return WeatherScreenViewModel(getWeatherUseCase)
    }

    @Test
    fun `when created should emit loading state first`() = runTest {
        // Given
        val viewModel = createViewModelWithDelay(Result.success(testWeather))

        // When & Then
        viewModel.state.test {
            assertEquals(WeatherScreenStatus.Loading, awaitItem().status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state flow should emit loading then success in sequence`() = runTest {
        // Given
        val viewModel = createViewModelWithDelay(Result.success(testWeather))

        // When & Then
        viewModel.state.test {
            assertEquals(WeatherScreenStatus.Loading, awaitItem().status)
            assertEquals(WeatherScreenStatus.Success, awaitItem().status)
        }
    }

    @Test
    fun `should show loading state when getWeather is called`() = runTest {
        // Given
        val viewModel = createViewModel(Result.success(testWeather))

        // When & Then
        viewModel.state.test {
            skipItems(2)

            viewModel.onEvent(WeatherScreenEvent.GetWeatherEvent)

            assertEquals(WeatherScreenStatus.Loading, awaitItem().status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getWeather should update state to success when use case returns data`() = runTest {
        // Given
        val expectedWeather = testWeather.toUiModel()
        val viewModel = createViewModelWithDelay(Result.success(testWeather))

        // When & Then
        viewModel.state.test {
            skipItems(1)
            val successState = awaitItem()
            assertEquals(WeatherScreenStatus.Success, successState.status)
            assertEquals(expectedWeather, successState.weatherData)
            assertEquals(null, successState.errorMessage)
        }
    }

    @Test
    fun `getWeather should update state to error when use case throws exception`() = runTest {
        // Given
        val exception = IOException(ERROR_MESSAGE_NETWORK)
        val viewModel = createViewModelWithDelay(Result.failure(exception))

        // When & Then
        viewModel.state.test {
            skipItems(1)

            val errorState = awaitItem()
            assertEquals(WeatherScreenStatus.Error, errorState.status)
            assertEquals(ERROR_MESSAGE_NETWORK, errorState.errorMessage)
            assertEquals(null, errorState.weatherData)
        }
    }

    @Test
    fun `getWeather should update state to error when flow emits exception`() = runTest {
        // Given
        coEvery { getWeatherUseCase.getWeather() } returns flow {
            delay(100)
            throw IOException(ERROR_MESSAGE_FLOW)
        }
        val viewModel = WeatherScreenViewModel(getWeatherUseCase)

        // When & Then
        viewModel.state.test {
            skipItems(1)

            val errorState = awaitItem()
            assertEquals(WeatherScreenStatus.Error, errorState.status)
            assertEquals(ERROR_MESSAGE_FLOW, errorState.errorMessage)
            assertEquals(null, errorState.weatherData)
        }
    }

    @Test
    fun `should call getWeather once when created`() = runTest {
        // Given & When
        createViewModel(Result.success(testWeather))

        // Then
        coVerify(exactly = 1) { getWeatherUseCase.getWeather() }
    }

    @Test
    fun `onEvent GetWeatherEvent should call getWeather twice`() = runTest {
        // Given
        val viewModel = createViewModel(Result.success(testWeather))

        // When
        viewModel.onEvent(WeatherScreenEvent.GetWeatherEvent)

        // Then
        coVerify (exactly = 2) { getWeatherUseCase.getWeather() }
    }

    @Test
    fun `onEvent DismissError should clear error message`() = runTest {
        // Given
        val viewModel = createViewModelWithDelay(
            Result.failure(IOException(ERROR_MESSAGE_TEST))
        )

        viewModel.state.test {
            skipItems(1)

            val errorState = awaitItem()
            assertEquals(ERROR_MESSAGE_TEST, errorState.errorMessage)

            // When
            viewModel.onEvent(WeatherScreenEvent.DismissError)

            // Then
            val stateAfterDismiss = awaitItem()
            assertEquals(null, stateAfterDismiss.errorMessage)
            assertEquals(WeatherScreenStatus.Error, stateAfterDismiss.status)
        }
    }

    @Test
    fun `state should emit loading before error`() = runTest {
        // Given
        val viewModel = createViewModelWithDelay(
            Result.failure(IOException("Error"))
        )

        // When & Then
        viewModel.state.test {
            assertEquals(WeatherScreenStatus.Loading, awaitItem().status)
            assertEquals(WeatherScreenStatus.Error, awaitItem().status)
        }
    }

    companion object {
        private const val ERROR_MESSAGE_NETWORK = "Network error"
        private const val ERROR_MESSAGE_FLOW = "Flow error"
        private const val ERROR_MESSAGE_TEST = "Test error"
    }
}