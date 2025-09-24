package com.example.weatherappmvi.presentation.favourites

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultFavouritesComponent @AssistedInject constructor(
    private val favouritesStoreFactory: FavouritesStoreFactory,
    @Assisted("onAddClick") private val onAddClick: () -> Unit,
    @Assisted("onSearchClick") private val onSearchClick: () -> Unit,
    @Assisted("onCityClick") private val onCityClick: (city: City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : FavouritesComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore { favouritesStoreFactory.create() }

    init {
        componentScope().launch {
            store.labels.collect {
                when(it) {
                    FavouritesStore.Label.ClickAdd -> {
                        onAddClick()
                    }
                    is FavouritesStore.Label.ClickCity -> {
                        onCityClick(it.city)
                    }
                    FavouritesStore.Label.ClickSearch -> {
                        onSearchClick()
                    }
                }
            }
        }

    }


    override fun onAddButtonClick() {
        store.accept(FavouritesStore.Intent.ClickAdd)
    }

    override fun onCityCLick(city: City) {
        store.accept(FavouritesStore.Intent.ClickCity(city))
    }

    override fun onSearchButtonClick() {
       store.accept( FavouritesStore.Intent.ClickSearch)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<FavouritesStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory{


        fun create(
            @Assisted("onAddClick") onAddClick: () -> Unit,
            @Assisted("onSearchClick") onSearchClick: () -> Unit,
            @Assisted("onCityClick") onCityClick: (city: City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultFavouritesComponent

    }
}