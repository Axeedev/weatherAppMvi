package com.example.weatherappmvi.presentation.root

import com.arkivanov.decompose.ComponentContext

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext{

}
