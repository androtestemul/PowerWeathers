package com.apska.presentation.screen.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: WeatherScreenViewModel = hiltViewModel()

    WeatherView(
        modifier = modifier,
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}
@Composable
fun WeatherView(
    modifier: Modifier = Modifier,
    state: WeatherScreenState = WeatherScreenState(),
    onEvent: (WeatherScreenEvent) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        onEvent(WeatherScreenEvent.GetWeatherEvent)
    }

    PullToRefreshBox(
        modifier = modifier.fillMaxSize().background(color = Color.Cyan),
        isRefreshing = state.status == WeatherScreenStatus.Loading,
        onRefresh = { onEvent(WeatherScreenEvent.GetWeatherEvent) }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(1) {
                Text(text = "ПОГОДА", modifier = Modifier.background(color = Color.Green))
            }
        }

    }

}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_7")
@Composable
fun GreetingPreview() {
    WeatherView()
}