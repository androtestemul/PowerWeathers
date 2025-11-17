package com.apska.presentation.screen.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apska.domain.usecase.GetWeatherUseCase
import com.apska.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherScreenViewModel @Inject constructor(
    private val getWeatherUseCase : GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherScreenState())
    val state: StateFlow<WeatherScreenState> = _state.asStateFlow()

    init {
        getWeather()
    }

    fun onEvent(event: WeatherScreenEvent) {
        when (event) {
            is WeatherScreenEvent.GetWeatherEvent -> getWeather()
            is WeatherScreenEvent.DismissError -> dismissError()
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    status = WeatherScreenStatus.Loading,
                    errorMessage = null
                )
            }

            getWeatherUseCase.getWeather()
                .catch { exception ->
                    _state.update {
                        it.copy(
                            status = WeatherScreenStatus.Error,
                            errorMessage = exception.message
                        )
                    }
                }
                .map { result ->
                    result.map { weather -> weather.toUiModel() }
                }
                .collect { result ->
                    result.onSuccess { weather ->
                        _state.update {
                            it.copy(
                                status = WeatherScreenStatus.Success,
                                weatherData = weather,
                                errorMessage = null
                            )
                        }
                    }.onFailure { exception ->
                        _state.update {
                            it.copy(
                                status = WeatherScreenStatus.Error,
                                errorMessage = exception.message
                            )
                        }
                    }
                }
        }

    }

    private fun dismissError() {
        _state.update { it.copy(errorMessage = null) }
    }
}