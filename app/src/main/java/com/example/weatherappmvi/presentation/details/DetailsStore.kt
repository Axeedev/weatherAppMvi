package com.example.weatherappmvi.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.entity.WeatherForecast
import com.example.weatherappmvi.domain.usecases.AddToFavouriteCitiesUseCase
import com.example.weatherappmvi.domain.usecases.GetForecastUseCase
import com.example.weatherappmvi.domain.usecases.ObserveIsFavouriteUseCase
import com.example.weatherappmvi.domain.usecases.RemoveFromFavouriteUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {


    data class State(
        val city: City,
        val isFavourite: Boolean,
        val status: Status
    ){

        sealed interface Status{
            data object Initial : Status

            data object Loading : Status

            data object Error : Status

            data class Loaded(
                val forecast: WeatherForecast
            ) : Status
        }
    }

    sealed interface Intent {

        data object SwitchStatus : Intent

        data object GoBack : Intent

    }

    sealed interface Label {

        data object GoBack : Label
    }


}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val observeIsFavouriteUseCase: ObserveIsFavouriteUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val addToFavouriteCitiesUseCase: AddToFavouriteCitiesUseCase,
    private val removeFromFavouriteUseCase: RemoveFromFavouriteUseCase,

) {


    fun create(city: City): DetailsStore = object : DetailsStore,
        Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = DetailsStore.State(
                city = city,
                status = DetailsStore.State.Status.Initial,
                isFavourite = false
            ),
            bootstrapper = BootstrapperImpl(city.id),
            executorFactory = {ExecutorImpl()},
            reducer = ReducerImpl()
        ) {}

    private sealed interface Action {

        data class FavouriteStatusLoaded(val isFavourite: Boolean) : Action

        data class ForecastLoaded(
            val forecast: WeatherForecast
        ) : Action

        data object LoadingStarted : Action

        data object LoadingFailed : Action

    }

    sealed interface Msg{


        data class FavouriteStatusLoaded(val isFavourite: Boolean) : Msg

        data class ForecastLoaded(
            val forecast: WeatherForecast
        ) : Msg

        data object LoadingStarted : Msg

        data object LoadingFailed : Msg

    }

    private inner class ExecutorImpl : CoroutineExecutor
    <DetailsStore.Intent, Action, DetailsStore.State, Msg, DetailsStore.Label>(){

        override fun executeAction(action: Action) {
            when(action) {
                is Action.FavouriteStatusLoaded -> {
                    dispatch(Msg.FavouriteStatusLoaded(action.isFavourite))
                }
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }
                Action.LoadingFailed -> {
                    dispatch(Msg.LoadingFailed)
                }
                Action.LoadingStarted -> {
                    dispatch(Msg.LoadingStarted)
                }
            }
        }

        override fun executeIntent(intent: DetailsStore.Intent) {
            when (intent) {
                DetailsStore.Intent.GoBack -> {
                    publish(DetailsStore.Label.GoBack)
                    
                }
                DetailsStore.Intent.SwitchStatus -> {
                    scope.launch {
                        val state = state()
                         if(state.isFavourite){
                             removeFromFavouriteUseCase(state.city.id)
                         }else{
                             addToFavouriteCitiesUseCase(state.city)
                         }
                    }
                }

            }
        }
    }
    private inner class BootstrapperImpl(
        private val cityId: Int
    ): CoroutineBootstrapper<Action>() {

        override fun invoke() {

            scope.launch {
                launch {
                    observeIsFavouriteUseCase(cityId).collect {
                        dispatch(Action.FavouriteStatusLoaded(it))
                    }
                }
                launch {
                    dispatch(Action.LoadingStarted)
                    try {
                        val forecast = getForecastUseCase(cityId)
                        dispatch(Action.ForecastLoaded(forecast))
                    } catch (e: Exception) {
                        dispatch(Action.LoadingFailed)
                    }
                }

            }
        }
    }
    private inner class ReducerImpl : Reducer<DetailsStore.State, Msg>{
        override fun DetailsStore.State.reduce(msg: Msg): DetailsStore.State {
            return when(msg) {
                is Msg.FavouriteStatusLoaded -> {
                    copy(isFavourite = msg.isFavourite)
                }
                is Msg.ForecastLoaded -> {

                    copy(status = DetailsStore.State.Status.Loaded(msg.forecast))
                }
                Msg.LoadingFailed -> {

                    copy(status = DetailsStore.State.Status.Error)
                }
                Msg.LoadingStarted -> {

                    copy(status= DetailsStore.State.Status.Loading)
                }
            }
        }
    }


}
