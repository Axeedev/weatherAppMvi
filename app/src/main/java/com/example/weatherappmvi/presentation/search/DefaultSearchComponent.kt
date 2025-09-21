package com.example.weatherappmvi.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultSearchComponent @AssistedInject constructor(
    private val storeFactory: SearchStoreFactory,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("onCitySavedToFavourite") private val onCitySavedToFavourite: () -> Unit,
    @Assisted("onCityForecastRequest") private val onCityForecastRequest: (City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext{


    private val store = storeFactory.create(openReason)


    init {
        componentScope().launch {
            store.labels.collect {
                when(it) {
                    SearchStore.Label.GoBack -> {
                        onBackClick.invoke()
                    }
                    SearchStore.Label.SavedToFavourite -> {
                        onCitySavedToFavourite()
                    }
                    is SearchStore.Label.ShowForecast -> {
                        onCityForecastRequest(it.city)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<SearchStore.State> = store.stateFlow

    override fun onSearchClick() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onQueryChange(query: String) {
        store.accept(SearchStore.Intent.ChangeQuery(query))
    }

    override fun onCityClick(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    override fun onBackClick() {
        store.accept(SearchStore.Intent.GoBack)
    }

    @AssistedFactory
    interface Factory{
        fun create(
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("onCitySavedToFavourite") onCitySavedToFavourite: () -> Unit,
            @Assisted("onCityForecastRequest") onCityForecastRequest: (City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultSearchComponent
    }

}