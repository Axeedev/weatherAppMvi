package com.example.weatherappmvi.data.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c") val averageTempC: Float,
    @SerializedName("condition") val conditionDto: ConditionDto
)
