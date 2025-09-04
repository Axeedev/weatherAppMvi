package com.example.weatherappmvi.domain.usecases

import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {

    operator fun invoke(): Flow<List<City>>{
        return favouriteRepository.favouriteCities
    }
}