package com.example.weatherappmvi.data.repository

import com.example.weatherappmvi.data.api.ApiService
import com.example.weatherappmvi.data.mappers.toWeatherEntity
import com.example.weatherappmvi.data.mappers.toWeatherForecast
import com.example.weatherappmvi.domain.entity.Weather
import com.example.weatherappmvi.domain.entity.WeatherForecast
import com.example.weatherappmvi.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val apiService: ApiService
): WeatherRepository {
    override suspend fun getWeatherByCity(id: Int): Weather {
        return apiService.getForecast("$PREFIX_CITY_ID$id").currentDay.toWeatherEntity()
    }

    override suspend fun getForecast(id: Int): WeatherForecast {
        return apiService.getForecast("$PREFIX_CITY_ID$id").toWeatherForecast()
    }

    private companion object{
        private const val PREFIX_CITY_ID = "id:"
    }
}