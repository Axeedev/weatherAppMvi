package com.example.weatherappmvi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface CitiesDao {

    @Query("Select * from cities")
    fun getCities(): Flow<List<CityDbModel>>

    @Query("SELECT EXISTS (SELECT * FROM cities WHERE id == :cityId LIMIT 1)")
    fun observeFavourite(cityId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(cityDbModel: CityDbModel)

    @Query("DELETE FROM cities WHERE id == :id")
    suspend fun removeFromFavourites(id: Int)

}