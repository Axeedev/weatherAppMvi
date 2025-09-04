package com.example.weatherappmvi.domain.usecases

import com.example.weatherappmvi.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
){

    suspend operator fun invoke(cityId: Int) = repository.getWeatherByCity(cityId)

}