package com.example.weatherappmvi.data.repository

import android.util.Log
import coil3.network.HttpException
import com.example.weatherappmvi.data.api.ApiService
import com.example.weatherappmvi.data.mappers.toEntity
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
        Log.d("SearchRepositoryImpl", "started")
        return try {
            apiService.getCities(query).map { it.toEntity() }
        }catch (e: Exception){
            Log.d("SearchRepositoryImpl", e.toString())
            listOf()
        }

    }
}