package com.example.weatherappmvi.data.repository

import com.example.weatherappmvi.data.local.CitiesDao
import com.example.weatherappmvi.data.local.CityDbModel
import com.example.weatherappmvi.data.mappers.toDbModel
import com.example.weatherappmvi.data.mappers.toEntity
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val dao: CitiesDao
): FavouriteRepository {
    override val favouriteCities: Flow<List<City>> = dao.getCities().map {
        it.map {cityDbModel ->
            cityDbModel.toEntity()
        }
    }

    override fun observeIsFavourite(id: Int): Flow<Boolean> {
       return dao.observeFavourite(id)
    }

    override suspend fun addToFavourite(city: City) {
        dao.addToFavourites(city.toDbModel())
    }

    override suspend fun removeFromFavourite(id: Int) {
        dao.removeFromFavourites(id)
    }
}

