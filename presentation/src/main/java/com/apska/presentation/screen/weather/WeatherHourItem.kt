package com.apska.presentation.screen.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apska.presentation.R
import com.apska.presentation.extentions.toHours
import com.apska.presentation.model.WeatherHourUi
import com.apska.presentation.ui.theme.PowerWeathersTheme
import com.apska.presentation.ui.view.NetworkImage

@Composable
fun WeatherHourItem(weatherHour: WeatherHourUi) {
    Column(
        modifier = Modifier
            //.height(120.dp)
            .width(70.dp)
    ) {
        Text(
            text = weatherHour.dateTime.toHours(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        NetworkImage(
            imageUrl = weatherHour.conditionIconUrl,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = weatherHour.conditionText,
                fontSize = 8.sp,
                lineHeight = 10.sp,
                softWrap = true,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.n_degrees_celsius, weatherHour.tempCurrent),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherHourItemPreview() {
    PowerWeathersTheme {
        WeatherHourItem(
            WeatherHourUi(
                dateTime = System.currentTimeMillis(),
                conditionText = "Умеренный дождь",
                conditionIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/296.png",
                tempCurrent = "21.6"
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun WeatherHourItemsRowPreview() {
    PowerWeathersTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) { index ->
                WeatherHourItem(
                    weatherHour = WeatherHourUi(
                        dateTime = System.currentTimeMillis() + index * 3600000,
                        conditionText = if (index % 2 == 0) "Ясно" else "Облачно",
                        conditionIconUrl = "https://cdn.weatherapi.com/weather/64x64/day/296.png",
                        tempCurrent = "${20 + index}"
                    )
                )
            }
        }
    }
}