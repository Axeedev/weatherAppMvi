package com.example.weatherappmvi.presentation.favourites

import com.example.weatherappmvi.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouritesComponent {


    val state : StateFlow<FavouritesStore.State>

    fun onSearchButtonClick()
    fun onAddButtonClick()
    fun onCityCLick(city: City)
}