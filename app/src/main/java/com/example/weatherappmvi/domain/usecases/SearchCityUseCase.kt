package com.example.weatherappmvi.domain.usecases

import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String): List<City>{
        return repository.search(query)
    }
}