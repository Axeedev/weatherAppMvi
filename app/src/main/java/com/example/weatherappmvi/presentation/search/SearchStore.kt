package com.example.weatherappmvi.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.usecases.AddToFavouriteCitiesUseCase
import com.example.weatherappmvi.domain.usecases.SearchCityUseCase
import com.example.weatherappmvi.presentation.search.SearchStore.Intent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject



interface SearchStore : Store<Intent, SearchStore.State, SearchStore.Label> {


    data class State(
        val query: String,
        val queryState: QueryState
    ) {
        sealed interface QueryState {

            data object Initial : QueryState

            data object Loading : QueryState

            data object Error : QueryState

            data object EmptyResult : QueryState

            data class Loaded(val cities: List<City>) : QueryState
        }
    }

    sealed interface Intent {

        data class ChangeQuery(val query: String) : Intent

        data object GoBack : Intent

        data object ClickSearch : Intent

        data class ClickCity(val city: City) : Intent

    }

    sealed interface Label {

        data object GoBack : Label

        data object SavedToFavourite : Label

        data class ShowForecast(val city: City) : Label

    }
}


class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addToFavouriteCitiesUseCase: AddToFavouriteCitiesUseCase,
    private val searchCityUseCase: SearchCityUseCase
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, SearchStore.State, SearchStore.Label>
        by storeFactory.create(
            initialState = SearchStore.State("", SearchStore.State.QueryState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = {
                ExecutorImpl(openReason)
            },
            reducer = ReducerImpl
        ) {}


    sealed interface Action

    sealed interface Msg {

        data class ChangeQuery(val query: String) : Msg

        data object LoadingStarted : Msg

        data object Error : Msg

        data class Loaded(val cities: List<City>) : Msg

    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(
        private val openReason: OpenReason
    ) : CoroutineExecutor<Intent, Action, SearchStore.State, Msg, SearchStore.Label>() {

        private var job : Job? = null

        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.ChangeQuery -> {
                    dispatch(Msg.ChangeQuery(intent.query))
                }

                is Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.ADD_TO_FAVOURITE -> {
                            scope.launch {
                                addToFavouriteCitiesUseCase(intent.city)
                                publish(SearchStore.Label.SavedToFavourite)
                            }

                        }

                        OpenReason.CHECK_WEATHER -> {
                            publish(SearchStore.Label.ShowForecast(intent.city))
                        }
                    }
                }

                Intent.ClickSearch -> {
                    job?.cancel()
                    job = scope.launch {
                        dispatch(Msg.LoadingStarted)

                        try {
                            val cities = searchCityUseCase(state().query)

                            dispatch(Msg.Loaded(cities))
                        } catch (_: Exception) {
                            dispatch(Msg.Error)
                        }
                    }
                }

                Intent.GoBack -> {
                    publish(SearchStore.Label.GoBack)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<SearchStore.State, Msg> {
        override fun SearchStore.State.reduce(msg: Msg): SearchStore.State {
            return when (msg) {
                is Msg.ChangeQuery -> {
                    copy(query = msg.query)
                }

                Msg.Error -> {
                    copy(queryState = SearchStore.State.QueryState.Error)
                }

                is Msg.Loaded -> {
                    if (msg.cities.isEmpty()){
                        copy(queryState = SearchStore.State.QueryState.Loaded(msg.cities))
                    }else copy(queryState = SearchStore.State.QueryState.EmptyResult)
                }

                Msg.LoadingStarted -> {

                    copy(queryState = SearchStore.State.QueryState.Loading)
                }
            }
        }
    }


}