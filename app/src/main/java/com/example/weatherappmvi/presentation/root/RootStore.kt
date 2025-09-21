package com.example.weatherappmvi.presentation.root

import com.arkivanov.mvikotlin.core.store.Store
import com.example.weatherappmvi.domain.entity.City

interface RootStore : Store<RootStore.Intent, RootStore.State, RootStore.Label> {

    sealed interface Intent

    sealed interface Label

    data class State(val unit: Unit)


}

