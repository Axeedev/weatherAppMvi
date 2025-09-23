package com.example.weatherappmvi.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.details.DefaultDetailsComponent
import com.example.weatherappmvi.presentation.favourites.DefaultFavouritesComponent
import com.example.weatherappmvi.presentation.search.DefaultSearchComponent
import com.example.weatherappmvi.presentation.search.OpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable

class DefaultRootComponent @AssistedInject constructor(
    private val defaultDetailsComponentFactory: DefaultDetailsComponent.Factory,
    private val defaultSearchComponentFactory: DefaultSearchComponent.Factory,
    private val defaultFavComponentFactory: DefaultFavouritesComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext{

    private val stackNavigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = stackNavigation,
        initialConfiguration = Config.Favourites,
        handleBackButton = true,
        serializer = Config.serializer(),
        childFactory = ::child
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child{
        return when(config){
            is Config.Details -> {
                val component = defaultDetailsComponentFactory.create(
                    city = config.city,
                    onBackClick = {
                        stackNavigation.pop()
                    },
                    componentContext = componentContext
                )

                RootComponent.Child.Details(component)


            }
            Config.Favourites -> {
                val component = defaultFavComponentFactory.create(
                    onAddClick = {
                        stackNavigation.push(Config.Search(OpenReason.ADD_TO_FAVOURITE))
                    },
                    onSearchClick = {
                        stackNavigation.push(Config.Search(OpenReason.CHECK_WEATHER))
                    },
                    onCityClick = {
                        stackNavigation.push(Config.Details(it))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Favourite(component)

            }
            is Config.Search -> {
                val component = defaultSearchComponentFactory.create(
                    openReason = config.openReason,
                    onBackClick = {
                        stackNavigation.pop()
                    },
                    onCitySavedToFavourite = {
                        stackNavigation.pop()
                    },
                    onCityForecastRequest = {
                        stackNavigation.push(Config.Details(it))
                    },
                    componentContext = componentContext
                )
                return RootComponent.Child.Search(component)

            }
        }

    }

    @Serializable
    private sealed interface Config{

        @Serializable
        data object Favourites : Config

        @Serializable
        data class Search(val openReason: OpenReason) : Config

        @Serializable
        data class Details(val city: City) : Config

    }


    @AssistedFactory
    interface Factory{
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ) : DefaultRootComponent
    }

}
