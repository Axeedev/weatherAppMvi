package com.example.weatherappmvi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase



@Database(
    entities = [CityDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao

}