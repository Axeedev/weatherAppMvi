package com.example.weatherappmvi.domain.entity

import java.util.Calendar

data class Weather(
    val tempC: Float,
    val condition: String,
    val conditionImageUrl: String,
    val date: Calendar
)
