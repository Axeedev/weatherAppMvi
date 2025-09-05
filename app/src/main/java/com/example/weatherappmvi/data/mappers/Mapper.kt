package com.example.weatherappmvi.data.mappers

import com.example.weatherappmvi.data.dto.CityDto
import com.example.weatherappmvi.data.dto.DayDto
import com.example.weatherappmvi.data.dto.WeatherDto
import com.example.weatherappmvi.data.dto.WeatherForecastDto
import com.example.weatherappmvi.data.local.CityDbModel
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.entity.Weather
import com.example.weatherappmvi.domain.entity.WeatherForecast
import java.util.Calendar
import java.util.Date


fun CityDbModel.toEntity() = City(
    id = id,
    name = name,
    country = country
)

fun City.toDbModel() = CityDbModel(
    id = id,
    name = name,
    country = country
)
fun CityDto.toEntity() = City(
    id = id,
    name = name,
    country = country
)
fun WeatherDto.toWeatherEntity() = Weather(
    tempC = tempC,
    condition = condition.text,
    conditionImageUrl = condition.iconUrl.toFormat(),
    date = lastUpdated.toCalendar()
)

fun WeatherForecastDto.toWeatherForecast() = WeatherForecast(
    weather = currentDay.toWeatherEntity(),
    forecast = forecastDto.forecast.toListWeather()
)

fun DayDto.toWeather()= Weather(
    tempC = this.dayWeatherDto.averageTempC,
    condition = this.dayWeatherDto.conditionDto.text,
    conditionImageUrl = this.dayWeatherDto.conditionDto.iconUrl.toFormat(),
    date = this.date.toCalendar()
)
fun List<DayDto>.toListWeather() : List<Weather> = map { it.toWeather() }



private fun String.toFormat() = "https::"+ this.replace(oldValue = "64x64", newValue = "128x128")

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar*1000)
}