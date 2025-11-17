package com.apska.presentation.extentions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.toFormattedDateTime(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.toDateMonthYear(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.toDateMonth(): String {
    val formatter = SimpleDateFormat("dd.MM", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.toHours(): String {
    val cal = Calendar.getInstance().apply {
        timeInMillis = this@toHours
    }
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    return "$hour:00"
}

fun Long.isMidnight(): Boolean {
    val cal = Calendar.getInstance().apply {
        timeInMillis = this@isMidnight
    }

    return cal.get(Calendar.HOUR_OF_DAY) == 0
}