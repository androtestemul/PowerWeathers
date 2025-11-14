package com.apska.presentation.screen.weather

sealed class WeatherScreenEvent {
    object GetWeatherEvent : WeatherScreenEvent()
    object DismissError : WeatherScreenEvent()
}