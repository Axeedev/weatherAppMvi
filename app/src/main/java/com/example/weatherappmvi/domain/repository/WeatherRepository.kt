package com.example.weatherappmvi.domain.repository

import com.example.weatherappmvi.domain.entity.Weather
import com.example.weatherappmvi.domain.entity.WeatherForecast

interface WeatherRepository {



    suspend fun getWeatherByCity(id: Int) : Weather
    suspend fun getForecast(id: Int): WeatherForecast


}