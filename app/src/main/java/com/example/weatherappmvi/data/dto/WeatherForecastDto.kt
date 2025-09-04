package com.example.weatherappmvi.data.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("current") val currentDay: WeatherDto,
    @SerializedName("forecast") val forecastDto: ForecastDayDto
) {
}