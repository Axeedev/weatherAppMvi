package com.example.weatherappmvi.presentation.favourites

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.usecases.GetCurrentWeatherUseCase
import com.example.weatherappmvi.domain.usecases.GetFavouriteCitiesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouritesStore: Store<
        FavouritesStore.Intent, FavouritesStore.State, FavouritesStore.Label > {

    data class State(
        val cities: List<CityItem>
    ){

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState{
            data object Error: WeatherState
            data object Initial : WeatherState
            data object Loading : WeatherState
            data class Loaded(
                val tempC: Float,
                val imageUrl: String
            ): WeatherState
        }
    }
    sealed interface Label{
        data object ClickSearch : Label

        data object ClickAdd : Label

        data class ClickCity(val city: City) : Label
    }

    sealed interface Intent{

        data object ClickSearch : Intent

        data object ClickAdd : Intent

        data class ClickCity(val city: City) : Intent
    }


}

class FavouritesStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase
){


    fun create() : FavouritesStore = object : FavouritesStore,
        Store<FavouritesStore.Intent, FavouritesStore.State, FavouritesStore.Label > by storeFactory.create(
        name = "FavouritesStoreFactory",
        initialState = FavouritesStore.State(listOf()),
        bootstrapper = BootstrapperImpl(),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ){}


    private sealed interface Action{

        data class FavCitiesLoaded(
            val cities: List<City>
        ): Action

    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect {
                    dispatch(Action.FavCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<FavouritesStore.Intent, Action, FavouritesStore.State, Msg, FavouritesStore.Label>(){

        override fun executeAction(action: Action) {
            when(action){
                is Action.FavCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavCitiesLoaded(cities))
                    cities.forEach{
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        override fun executeIntent(intent: FavouritesStore.Intent) {
            when(intent) {
                FavouritesStore.Intent.ClickAdd -> {
                    publish(FavouritesStore.Label.ClickAdd)
                }
                is FavouritesStore.Intent.ClickCity -> {
                    publish(FavouritesStore.Label.ClickCity(intent.city))
                }
                FavouritesStore.Intent.ClickSearch -> {
                    publish(FavouritesStore.Label.ClickSearch)
                }
            }
        }
        private suspend fun loadWeatherForCity(city: City){
            dispatch(Msg.WeatherLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase(city.id)
                dispatch(Msg.WeatherLoaded(
                    id = city.id,
                    tempC = weather.tempC,
                    imageUrl = weather.conditionImageUrl
                ))
            } catch (e: Exception) {
                Log.d("FavouritesStoreFactory", e.message ?: "")
                dispatch(Msg.WeatherLoadingError(city.id))
            }
        }
    }

    private sealed interface Msg{
        data class FavCitiesLoaded(
            val cities: List<City>
        ): Msg


        data class WeatherLoaded(
            val id: Int,
            val tempC: Float,
            val imageUrl: String
        ): Msg


        data class WeatherLoadingError(val id: Int): Msg

        data class WeatherLoading(val id: Int): Msg
    }
    private object ReducerImpl : Reducer<FavouritesStore.State, Msg> {
        override fun FavouritesStore.State.reduce(
            msg: Msg
        ): FavouritesStore.State {
            return when(msg){
                is Msg.FavCitiesLoaded -> {
                    copy(cities = msg.cities.map {
                        FavouritesStore.State.CityItem(it, FavouritesStore.State.WeatherState.Initial)
                    })
                }
                is Msg.WeatherLoaded -> {
                    copy(
                        cities = cities.map {
                            if (it.city.id == msg.id){
                                it.copy(weatherState = FavouritesStore.State.WeatherState.Loaded(
                                    tempC = msg.tempC,
                                    imageUrl = msg.imageUrl
                                ))
                            } else it
                        }
                    )
                }
                is Msg.WeatherLoading -> {
                    copy(
                        cities = cities.map {
                            if (it.city.id == msg.id){
                                it.copy(
                                    weatherState = FavouritesStore.State.WeatherState.Loading
                                )
                            }else it
                        }
                    )
                }
                is Msg.WeatherLoadingError -> {
                    copy(
                        cities = cities.map {
                            if (it.city.id == msg.id){
                                it.copy(
                                    weatherState = FavouritesStore.State.WeatherState.Error
                                )
                            }else it
                        }
                    )
                }
            }
        }
    }

}