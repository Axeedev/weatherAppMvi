package com.example.weatherappmvi.presentation.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FavouriteContent(
    modifier: Modifier = Modifier,
    favouritesComponent: FavouritesComponent
){

    val state by favouritesComponent.state.collectAsState()
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(state.cities){index, item ->
            CityCard(city = item)
        }
    }
}


@Composable
fun CityCard(
    modifier: Modifier = Modifier,
    city: FavouritesStore.State.CityItem
){
    Card(
        modifier = modifier
            .fillMaxSize(),
        shape = MaterialTheme.shapes.extraLarge
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sizeIn(minHeight = 196.dp)
                .padding(all = 24.dp)
        ){
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                text = city.city.name)
        }

    }
}