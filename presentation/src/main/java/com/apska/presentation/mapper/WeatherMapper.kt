package com.apska.presentation.mapper

import com.apska.domain.model.Weather
import com.apska.domain.model.WeatherDay
import com.apska.domain.model.WeatherHour
import com.apska.presentation.model.WeatherDayUi
import com.apska.presentation.model.WeatherHourUi
import com.apska.presentation.model.WeatherUi

fun WeatherHour.toUiModel() = WeatherHourUi(
    dateTime = dateTime,
    conditionText = conditionText,
    conditionIconUrl = conditionIconUrl,
    tempCurrent = tempCurrent
)

fun WeatherDay.toUiModel() = WeatherDayUi(
    date = date,
    conditionIconUrl = conditionIconUrl,
    tempAvg = tempAvg
)

fun Weather.toUiModel() = WeatherUi(
    city = city,
    region = region,
    country = country,
    lastUpdated = lastUpdated,
    condition = condition,
    tempCurrent = tempCurrent,
    tempMin = tempMin,
    tempMax = tempMax,
    windSpeed = windSpeed,
    windDirection = windDirection,
    windDegree = windDegree,
    hourList = hourList.map { it.toUiModel() },
    dayList = dayList.map { it.toUiModel() }
)