package com.apska.presentation.screen.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apska.presentation.model.WeatherDayUi
import com.apska.presentation.model.WeatherHourUi
import com.apska.presentation.model.WeatherUi
import com.apska.presentation.ui.theme.DARK_BLUE
import com.apska.presentation.ui.theme.DARK_BLUE_GRAY
import com.apska.presentation.ui.theme.PowerWeathersTheme

@Composable
fun WeatherScreen(
    innerPadding: PaddingValues
) {
    val viewModel: WeatherScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    WeatherView(
        innerPadding = innerPadding,
        state = state,
        onEvent = viewModel::onEvent
    )
}
@Composable
private fun WeatherView(
    innerPadding: PaddingValues = PaddingValues(),
    state: WeatherScreenState = WeatherScreenState(),
    onEvent: (WeatherScreenEvent) -> Unit = {}
) {
    val showData = state.weatherData != null

    PullToRefreshBox(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(DARK_BLUE),
                        Color(DARK_BLUE_GRAY)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            .padding(horizontal = 8.dp),
        isRefreshing = state.status == WeatherScreenStatus.Loading,
        onRefresh = { onEvent(WeatherScreenEvent.GetWeatherEvent) }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight() // Занимает всю высоту LazyColumn
                ) {
                    if (showData) {
                        ScreenSuccess(innerPadding,state.weatherData)
                    } else {
                        when (state.status) {
                            WeatherScreenStatus.Loading -> ScreenLoading()
                            else -> ScreenEmpty()
                        }
                    }
                }
            }
        }

        if (state.errorMessage != null) {
            ErrorDialog(
                message = state.errorMessage,
                onDismiss = { onEvent(WeatherScreenEvent.DismissError) },
                onRetry = { onEvent(WeatherScreenEvent.GetWeatherEvent) }
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "id:pixel_7",
    locale = "ru"
)
@Composable
private fun WeatherViewPreview() {
    val dateTime = System.currentTimeMillis()

    val hourList = buildList {
        repeat(9) { index ->
            add(
                WeatherHourUi(
                    dateTime = dateTime + index * 3600000,
                    conditionText = if (index % 2 == 0) "Ясно" else "Облачно",
                    conditionIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/296.png",
                    tempCurrent = "${20 + index}"
                )
            )
        }
    }

    val dayList = buildList {
        repeat(3) { index ->
            add(
                WeatherDayUi(
                    date = dateTime + index * 3600000,
                    conditionIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/296.png",
                    tempAvg = "${20.7 + index}"
                )
            )
        }
    }
    PowerWeathersTheme {
        WeatherView(
            state = WeatherScreenState(
                status = WeatherScreenStatus.Success,
                weatherData = WeatherUi(
                    city = "Москва",
                    region = "Moscow City",
                    country = "Russia",
                    lastUpdated = dateTime,
                    condition = "Переменная облачность",
                    tempCurrent = "3.1",
                    tempMin = "5.2",
                    tempMax = "6.1",
                    windSpeed = "5.4",
                    windDirection = "SE",
                    windDegree = 45f,
                    hourList = hourList,
                    dayList = dayList
                )
            )
        )
    }
}

