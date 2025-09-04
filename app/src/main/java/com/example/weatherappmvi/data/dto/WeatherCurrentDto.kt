package com.example.weatherappmvi.data.dto

import com.google.gson.annotations.SerializedName

data class WeatherCurrentDto(
    @SerializedName("current") val weather: WeatherDto
)
