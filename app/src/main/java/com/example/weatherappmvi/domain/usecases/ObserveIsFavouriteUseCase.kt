package com.example.weatherappmvi.domain.usecases

import com.example.weatherappmvi.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {
    operator fun invoke(id: Int): Flow<Boolean> {
        return repository.observeIsFavourite(id)
    }
}