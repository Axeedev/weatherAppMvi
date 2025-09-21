package com.example.weatherappmvi.presentation.search

import com.example.weatherappmvi.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val state : StateFlow<SearchStore.State>

    fun onQueryChange(query: String)

    fun onBackClick()

    fun onSearchClick()

    fun onCityClick(city: City)

}