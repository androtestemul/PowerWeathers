package com.apska.presentation.screen.weather

import android.util.Log
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
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            _state.update { it.copy(status = WeatherScreenStatus.Loading) }

            getWeatherUseCase.getWeather()
                .catch { exception ->
                    _state.update { it.copy(status = WeatherScreenStatus.Error(exception.message)) }
                    Log.e("STATE", "Error getting weather", exception)
                }
                .map { result ->
                    result.map { weather -> weather.toUiModel() }
                }
                .collect { result ->
                    result.onSuccess { weather ->
                        _state.update { it.copy(status = WeatherScreenStatus.Success(weather)) }
                    }.onFailure { exception ->
                        _state.update { it.copy(status = WeatherScreenStatus.Error(exception.message)) }
                    }
                }
            Log.d("STATE", "getWeather: ${_state.value}")
        }

    }
}