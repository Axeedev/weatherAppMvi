package com.example.weatherappmvi.domain.entity

data class WeatherForecast(
    val weather: Weather,
    val forecast: List<Weather>
)
