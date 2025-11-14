package com.apska.presentation.screen.weather

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.apska.presentation.R
import com.apska.presentation.ui.theme.PowerWeathersTheme

@Composable
fun ErrorDialog(
    message: String?,
    textTitle: String = stringResource(R.string.error_title),
    textError: String = stringResource(R.string.error_text, message ?: ""),
    textConfirmButton: String = stringResource(R.string.error_refresh),
    textDismissButton: String = stringResource(R.string.error_cancel),
    onDismiss: () -> Unit = {},
    onRetry: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = textTitle)
        },
        text = {
            Text(text = textError)
        },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(textConfirmButton)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(textDismissButton)
            }
        }
    )
}

@Preview(
    showBackground = true,
    device = "id:pixel_7",
    locale = "ru"
)
@Composable
private fun ErrorDialogPreview() {
    PowerWeathersTheme {
        ErrorDialog(
            message = "Произошла ошибка!",
            textTitle = "Ошибка",
            textError = "Произошла ошибка, попробуйте обновить данные позже.\n\nОшибка загрузки данных.",
            textConfirmButton = "Обновить",
            textDismissButton = "Отмена"
        )
    }
}