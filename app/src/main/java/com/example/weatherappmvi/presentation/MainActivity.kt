package com.example.weatherappmvi.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.weatherappmvi.presentation.root.DefaultRootComponent
import com.example.weatherappmvi.presentation.root.RootContent
import com.example.weatherappmvi.ui.theme.WeatherAppMVITheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var factory: DefaultRootComponent.Factory


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)



        enableEdgeToEdge()
        val root = factory.create(defaultComponentContext())
        setContent {
            WeatherAppMVITheme {
                RootContent(root)
            }
        }
    }
}