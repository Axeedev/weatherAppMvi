package com.example.weatherappmvi.domain.usecases

import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.FavouriteRepository
import javax.inject.Inject

class AddToFavouriteCitiesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(city: City){
        repository.addToFavourite(city)
    }
}