package com.example.weatherappmvi.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.weatherappmvi.presentation.favourites.FavouriteContent

@Composable
fun RootContent(component: RootComponent){
    Children(
        stack = component.stack
    ) {
        when(val instance =  it.instance){
            is RootComponent.Child.Details -> {

            }
            is RootComponent.Child.Favourite -> {
                FavouriteContent(favouritesComponent = instance.component)
            }
            is RootComponent.Child.Search -> {

            }
        }
    }
}