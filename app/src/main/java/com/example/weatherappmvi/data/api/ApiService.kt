package com.example.weatherappmvi.data.api

import com.example.weatherappmvi.BuildConfig
import com.example.weatherappmvi.data.dto.CityDto
import com.example.weatherappmvi.data.dto.WeatherCurrentDto
import com.example.weatherappmvi.data.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") query: String
    ): WeatherCurrentDto

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("days") days: Int = 3
    ): WeatherForecastDto

    @GET("search.json")
    suspend fun getCities(
        @Query("q") query: String,
    ) : List<CityDto>


}