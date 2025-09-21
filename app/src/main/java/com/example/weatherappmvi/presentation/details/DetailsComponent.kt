package com.example.weatherappmvi.presentation.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {


    val state: StateFlow<DetailsStore.State>


    fun onBackClick()

    fun onClickChangeFavouriteStatus()

}