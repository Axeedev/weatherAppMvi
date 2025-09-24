package com.example.weatherappmvi.presentation.favourites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weatherappmvi.ui.theme.GradientUtil


@Composable
fun FavouriteContent(
    modifier: Modifier = Modifier,
    favouritesComponent: FavouritesComponent
) {

    val state by favouritesComponent.state.collectAsState()

        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item(span = {
                GridItemSpan(2)
            }) {
                SearchButton { favouritesComponent.onSearchButtonClick() }
            }
            itemsIndexed(state.cities, key = { _, city ->
                city.city.id
            }) { index, item ->
                CityCard(
                    city = item, index = index){
                    favouritesComponent.onCityCLick(item.city)
                }
            }
            item {
                ButtonAdd { favouritesComponent.onAddButtonClick() }
            }

    }

}


@Composable
fun CityCard(
    modifier: Modifier = Modifier,
    index: Int,
    city: FavouritesStore.State.CityItem,
    onCityClick: () -> Unit
) {
    val gradient = GradientUtil.getGradientByIndex(index)

    Card(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                elevation = 16.dp,
                spotColor = gradient.shadow,
                shape = MaterialTheme.shapes.extraLarge
            ).clickable{
                onCityClick.invoke()
            },
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient.primaryGradient)
                .sizeIn(minHeight = 196.dp)
                .padding(all = 24.dp)

        ) {
            when (val state = city.weatherState) {
                FavouritesStore.State.WeatherState.Error -> {

                }

                FavouritesStore.State.WeatherState.Initial -> {

                }

                is FavouritesStore.State.WeatherState.Loaded -> {
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 32.dp),
                        text = "${state.tempC.toInt()}°С",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 32.sp
                    )
                    AsyncImage(
                        model = state.imageUrl,
                        contentDescription = "weather for city: ${city.city.name}",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(50.dp)
                    )
                }

                FavouritesStore.State.WeatherState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp),
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart),
                text = city.city.name,
                color = MaterialTheme.colorScheme.background
            )

        }

    }
}

@Composable
fun ButtonAdd(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .sizeIn(minHeight = 196.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        shape = MaterialTheme.shapes.extraLarge){
        Column(
            modifier = Modifier
                .sizeIn(minHeight = 196.dp)
                .fillMaxSize()
                .clickable{onClick.invoke()},
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(32.dp)
                    .size(50.dp),
                imageVector = Icons.Default.Create,
                contentDescription = "",

                )
            Spacer(modifier = Modifier.weight(2f))
            Text(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Add favourite",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Composable
fun SearchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 48.dp).clip(CircleShape),
        shape = CircleShape
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{
                    onClick.invoke()}
                .background(GradientUtil.buttonGradient.primaryGradient),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Icon(
                modifier = Modifier
                    .padding(all = 16.dp),
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.background
            )
            Text(

                text = "Search",
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}