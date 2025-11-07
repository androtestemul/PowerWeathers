package com.apska.presentation.screen.weather

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apska.domain.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherScreenViewModel @Inject constructor(
    private val getWeatherUseCase : GetWeatherUseCase
) : ViewModel() {

    var state by mutableStateOf(WeatherScreenState())
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    fun onEvent(event: WeatherScreenEvent) {
        when (event) {
            is WeatherScreenEvent.GetWeatherEvent -> getWeather()
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            state = state.copy(status = WeatherScreenStatus.Loading)
            getWeatherUseCase.getWeather().collect { result ->
                result.onSuccess { weather ->
                    state = state.copy(status = WeatherScreenStatus.Success(weather))
                }.onFailure { exception ->
                    state = state.copy(status = WeatherScreenStatus.Error(exception.message))
                }
            }
            Log.d("STATE", "getWeather: $state")
        }

    }

    fun isLoading() : Boolean {
        return state.status == WeatherScreenStatus.Loading
    }
}