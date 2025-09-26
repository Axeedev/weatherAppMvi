package com.example.weatherappmvi.data.repository

import android.util.Log
import com.example.weatherappmvi.data.api.ApiService
import com.example.weatherappmvi.data.mappers.toWeatherEntity
import com.example.weatherappmvi.data.mappers.toWeatherForecast
import com.example.weatherappmvi.domain.entity.Weather
import com.example.weatherappmvi.domain.entity.WeatherForecast
import com.example.weatherappmvi.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeatherByCity(id: Int): Weather {
        return apiService.getForecast("$PREFIX_CITY_ID$id").currentDay.toWeatherEntity()
    }

    override suspend fun getForecast(id: Int): WeatherForecast {
        return apiService.getForecast("$PREFIX_CITY_ID$id")
            .also { Log.d("WeatherRepositoryImpl", it.forecastDto.forecast.joinToString("\n")) }
            .toWeatherForecast()
    }

    private companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}