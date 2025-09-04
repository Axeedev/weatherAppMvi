package com.example.weatherappmvi.domain.usecases

import com.example.weatherappmvi.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(id: Int) = repository.getForecast(id)
}