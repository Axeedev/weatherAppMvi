package com.example.weatherappmvi.presentation.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.ui.theme.GradientUtil


@Composable
fun SearchContent(
    component: SearchComponent
) {
    val state by component.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GradientUtil.buttonGradient.primaryGradient)
    ) {
        Search(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp),
            value = state.query,
            onSearchPressed = {component.onSearchClick()},
            onBackPressed = {component.onBackClick()},
            onValueChange = {component.onQueryChange(it)}
        )
    }

    when (val status = state.queryState) {
        SearchStore.State.QueryState.EmptyResult -> {

        }

        SearchStore.State.QueryState.Error -> {

        }

        SearchStore.State.QueryState.Initial -> {

        }

        is SearchStore.State.QueryState.Loaded -> {


        }
        SearchStore.State.QueryState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}


@Composable
fun Search(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onBackPressed: () -> Unit,
    onSearchPressed: () -> Unit
) {
    TextField(
        modifier = modifier
            .border(BorderStroke(1.5.dp,
                color = MaterialTheme.colorScheme.background), shape = RoundedCornerShape(12.dp)
            ),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .clickable{
                        onBackPressed.invoke()
                    },
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = "go back"
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable{
                        onSearchPressed.invoke()
                    },
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = "go back"
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.background
        ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun SearchCityCard(
    modifier: Modifier = Modifier,
    city: City
){

}