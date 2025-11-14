package com.apska.presentation.screen.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apska.presentation.extentions.toDateMonthYear
import com.apska.presentation.model.WeatherDayUi
import com.apska.presentation.ui.theme.PowerWeathersTheme
import com.apska.presentation.ui.view.NetworkImage

@Composable
fun WeatherDayItem(weatherDay: WeatherDayUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = weatherDay.date.toDateMonthYear())
        Spacer(modifier = Modifier.weight(1f))
        NetworkImage(
            imageUrl = weatherDay.conditionIconUrl,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(text = "${weatherDay.tempAvg} °C")
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherDayItemPreview() {
    PowerWeathersTheme {
        WeatherDayItem(
            WeatherDayUi(
                date = System.currentTimeMillis(),
                conditionIconUrl = "https://cdn.weatherapi.com/weather/64x64/night/122.png",
                tempAvg = "21.6"
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun WeatherDayItemsRowPreview() {
    PowerWeathersTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) { index ->
                WeatherDayItem(
                    weatherDay = WeatherDayUi(
                        date = System.currentTimeMillis() + index * 24 * 3600000,
                        conditionIconUrl = "https://cdn.weatherapi.com/weather/64x64/night/122.png",
                        tempAvg = "${20 + index}"
                    )
                )
            }
        }
    }
}