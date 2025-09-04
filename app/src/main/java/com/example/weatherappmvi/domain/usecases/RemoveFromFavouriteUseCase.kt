package com.example.weatherappmvi.domain.usecases

import com.example.weatherappmvi.domain.repository.FavouriteRepository
import javax.inject.Inject

class RemoveFromFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    suspend operator fun invoke(id: Int){
        repository.removeFromFavourite(id)
    }
}