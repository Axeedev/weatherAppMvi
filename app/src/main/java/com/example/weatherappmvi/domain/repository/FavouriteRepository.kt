package com.example.weatherappmvi.domain.repository

import com.example.weatherappmvi.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    val favouriteCities: Flow<List<City>>

    fun observeIsFavourite(id: Int) : Flow<Boolean>

    suspend fun addToFavourite(city: City)
    suspend fun removeFromFavourite(id: Int)
}