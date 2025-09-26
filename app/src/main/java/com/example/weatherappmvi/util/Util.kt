package com.example.weatherappmvi.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Float.toWeatherFormat() = "${this.toInt()}°C"


fun Calendar.toFormatedFull(): String{
    val format = SimpleDateFormat("EEEE | d MMM y", Locale.getDefault())
    return format.format(time)
}

fun Calendar.toFormatedShort(): String{
    val format = SimpleDateFormat("EEE", Locale.getDefault())
    return format.format(time)
}