package com.example.weatherappmvi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cities")
data class CityDbModel(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String
)
