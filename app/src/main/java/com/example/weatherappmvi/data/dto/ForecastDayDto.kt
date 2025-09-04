package com.example.weatherappmvi.data.dto

import com.google.gson.annotations.SerializedName

data class ForecastDayDto(
    @SerializedName("forecastday") val forecast:List<DayDto>
)
