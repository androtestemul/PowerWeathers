package com.apska.presentation.screen.weather

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apska.presentation.R
import com.apska.presentation.extentions.toFormattedDateTime
import com.apska.presentation.model.WeatherDayUi
import com.apska.presentation.model.WeatherHourUi
import com.apska.presentation.model.WeatherUi

@Composable
fun ScreenSuccess(
    innerPadding: PaddingValues,
    weather: WeatherUi
) {
    ProvideTextStyle(
        value = TextStyle(
            color = Color.White,
            fontSize = 20.sp
        )
    ) {
        val scrollState = rememberScrollState()
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = innerPadding.calculateTopPadding())
                .verticalScroll(state = scrollState)
        ) {
            CurrentWeather(weather)
            WeatherPerHour(weather.hourList)
            WeatherPerDay(weather.dayList)
        }
    }
}

@Composable
fun CurrentWeather(weather: WeatherUi) {
    WeatherSurface {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.current_weather),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row {
                Text(text = weather.city)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = weather.lastUpdated.toFormattedDateTime())
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${weather.tempCurrent} °C",
                    fontSize = 65.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "min ${weather.tempMin} °C")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "max ${weather.tempMax} °C")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = weather.condition,
                fontSize = 32.sp,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.wind_speed_kph, weather.windSpeed))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.wind_direction, weather.windDirection))
        }
    }
}

@Composable
fun WeatherPerHour(hourList: List<WeatherHourUi>) {
    WeatherSurface {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.forecast_per_hour),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )


            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier.horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                hourList.forEach { weatherHourUi ->
                    WeatherHourItem(weatherHourUi)
                }
            }
        }
    }
}

@Composable
fun WeatherPerDay(dayList: List<WeatherDayUi>) {
    WeatherSurface {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.n_day_forecast, dayList.size),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            dayList.forEachIndexed { index, weatherDayUi ->
                WeatherDayItem(weatherDayUi)

                if (index != dayList.size-1) {
                    HorizontalDivider(thickness = 2.dp)
                }
            }

        }
    }
}

@Composable
private fun WeatherSurface(
    content : @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        color = Color(0xFF121923).copy(alpha = 0.1f)
    ) {
        content()
    }
}