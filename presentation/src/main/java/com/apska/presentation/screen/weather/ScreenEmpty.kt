package com.apska.presentation.screen.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apska.presentation.R
import com.apska.presentation.ui.theme.DARK_BLUE_GRAY
import com.apska.presentation.ui.theme.PowerWeathersTheme

@Composable
fun ScreenEmpty(
    text: String = stringResource(R.string.no_weather_data)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true,
    device = "id:pixel_7",
    locale = "ru",
    backgroundColor = DARK_BLUE_GRAY
)
@Composable
private fun ScreenEmptyPreview() {
    PowerWeathersTheme {
        ScreenEmpty("Нет данных о погоде.\nПопробуйте обновить данные позже.")
    }
}