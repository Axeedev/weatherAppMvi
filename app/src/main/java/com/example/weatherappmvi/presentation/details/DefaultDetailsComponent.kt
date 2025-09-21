package com.example.weatherappmvi.presentation.details

import androidx.compose.animation.core.DeferredTargetAnimation
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
import kotlinx.coroutines.launch

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext{

    private val store = instanceKeeper.getStore { storeFactory.create(city) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    DetailsStore.Label.GoBack -> {
                        onBackClick.invoke()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onBackClick() {
        store.accept(DetailsStore.Intent.GoBack)
    }

    override fun onClickChangeFavouriteStatus() {
        store.accept(DetailsStore.Intent.SwitchStatus)
    }

    @AssistedFactory
    interface Factory{

        fun create(

            @Assisted("city") city: City,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }

}