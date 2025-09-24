package com.example.weatherappmvi.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weatherappmvi.ui.theme.GradientUtil
import com.example.weatherappmvi.util.toWeatherFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    detailsComponent: DetailsComponent
){
    val state by detailsComponent.state.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        modifier = modifier
            .background(GradientUtil.detailsBackgroundGradient.primaryGradient),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {
                    Text(
                        text = state.city.name,
                        color = MaterialTheme.colorScheme.background,
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 48.dp)
                            .clickable{
                                detailsComponent.onBackClick()
                            },
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "go back",
                        tint = MaterialTheme.colorScheme.background
                    )
                },
                actions = {
                    if (state.isFavourite){
                        Icon(
                            modifier = Modifier
                                    .padding(end = 16.dp)
                                .clip(CircleShape)
                                .clickable{detailsComponent.onClickChangeFavouriteStatus()},
                            imageVector = Icons.Default.Star,
                            contentDescription = "change favourite status",
                            tint = MaterialTheme.colorScheme.background
                        )
                    }else{
                        Icon(
                            modifier = Modifier
                                    .padding(end = 16.dp)
                                .clip(CircleShape)
                                .clickable{detailsComponent.onClickChangeFavouriteStatus()},
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "change favourite status",
                        )
                    }

                }
            )
        },


    )  {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            when(val status = state.status) {
                DetailsStore.State.Status.Error -> {

                }
                DetailsStore.State.Status.Initial -> {

                }
                is DetailsStore.State.Status.Loaded -> {
                    Text(
                        text = status.forecast.weather.condition,
                        color = MaterialTheme.colorScheme.background
                    )
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = status.forecast.weather.tempC.toWeatherFormat(),
                            fontSize = 48.sp,
                            color = MaterialTheme.colorScheme.background
                        )
                        AsyncImage(
                            model = status.forecast.weather.conditionImageUrl.replace(oldValue = "::", newValue = ":"),
                            contentDescription = "weather image"
                        )
                    }

                }
                DetailsStore.State.Status.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }

    }


}