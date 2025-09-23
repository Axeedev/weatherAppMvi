package com.example.weatherappmvi.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.weatherappmvi.presentation.details.DetailsComponent
import com.example.weatherappmvi.presentation.favourites.FavouritesComponent
import com.example.weatherappmvi.presentation.search.SearchComponent

interface RootComponent {


    val stack: Value<ChildStack<*, Child>>

    sealed interface Child{

        data class Favourite(val component: FavouritesComponent) : Child

        data class Details(val component: DetailsComponent) : Child

        data class Search(val component: SearchComponent) : Child


    }

}