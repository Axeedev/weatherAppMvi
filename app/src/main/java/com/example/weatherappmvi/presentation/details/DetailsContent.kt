package com.example.weatherappmvi.presentation.details

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.weatherappmvi.domain.entity.Weather
import com.example.weatherappmvi.domain.entity.WeatherForecast
import com.example.weatherappmvi.ui.theme.GradientUtil
import com.example.weatherappmvi.util.toFormatedFull
import com.example.weatherappmvi.util.toFormatedShort
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

                    Spacer(Modifier.weight(1f))
                    Text(
                        text = status.forecast.weather.condition,
                        color = MaterialTheme.colorScheme.background
                    )
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = status.forecast.weather.tempC.toWeatherFormat(),
                            fontSize = 56.sp,
                            color = MaterialTheme.colorScheme.background
                        )
                        AsyncImage(
                            modifier = Modifier.size(70.dp),
                            model = status.forecast.weather.conditionImageUrl.replace(oldValue = "::", newValue = ":"),
                            contentDescription = "weather image"
                        )
                    }
                    Text(
                        text = status.forecast.weather.date.toFormatedFull(),
                        color = MaterialTheme.colorScheme.background
                    )

                    Spacer(Modifier.weight(1f))
                    AnimatedUpcomingWeather(status.forecast)
                    Spacer(Modifier.weight(1f))

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

@Composable
fun Forecast(
    modifier: Modifier = Modifier,
    forecast: WeatherForecast
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.2f))

    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp),
                fontSize = 24.sp,
                text = "Upcoming",
                color = MaterialTheme.colorScheme.background
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                forecast.forecast.forEach { weather ->
                    ForecastWeatherDayCard(weather = weather)
                    Log.d("Forecast", weather.toString() + "\n")
                }
            }
        }

    }
}

@Composable
fun ForecastWeatherDayCard(
    modifier: Modifier = Modifier,
    weather: Weather
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.tempC.toWeatherFormat(),
                fontWeight = FontWeight.Bold
            )
            AsyncImage(
                model = weather.conditionImageUrl.replace(oldValue = "::", ":"),
                contentDescription = "Image of weather condition"
            )
            Text(
                text = weather.date.toFormatedShort(),
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Composable
fun AnimatedUpcomingWeather(
    forecast: WeatherForecast
){
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = { IntOffset(0,it.height) }
        )
    ) {
        Forecast(forecast = forecast)
    }
}