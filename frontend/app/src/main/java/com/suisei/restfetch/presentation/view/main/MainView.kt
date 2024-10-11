package com.suisei.restfetch.presentation.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.suisei.restfetch.presentation.intent.MainIntent
import com.suisei.restfetch.presentation.state.MainViewState
import com.suisei.restfetch.presentation.view.Notify
import com.suisei.restfetch.presentation.view.theme.backgroundColor
import com.suisei.restfetch.presentation.view.theme.bottomAppBarBackgroundColor
import com.suisei.restfetch.presentation.view.theme.buttonTransparentTheme
import com.suisei.restfetch.presentation.viewmodel.MainViewModel
import com.suisei.restfetch.presentation.viewmodel.NotifyViewModel

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val viewState = viewModel.state.collectAsState()

    Notify()

    LaunchedEffect(Unit) {

    }
    Column(
        modifier = Modifier
            .background(backgroundColor()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (val state = viewState.value) {
            is MainViewState.Home -> HomeScreen(state.homeState)
            is MainViewState.MyPage -> MyPageScreen()
        }
    }


}

@Composable
fun BottomAppBar() {
    val viewModel: MainViewModel = hiltViewModel()
    val homeViewState = viewModel.lastHomeViewState
    val myPageViewState = viewModel.lastMyPageState

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .background(bottomAppBarBackgroundColor()),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BottomAppBarMenu(
            imageVector = Icons.Outlined.Home,
            description = "Home",
            MainIntent.LoadHome,
            MainViewState.Home(homeViewState),
            modifier = Modifier.weight(1f)
        )
        BottomAppBarMenu(
            imageVector = Icons.Outlined.Person,
            description = "MyPage",
            MainIntent.LoadMyPage,
            MainViewState.MyPage(myPageViewState),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BottomAppBarMenu(
    imageVector: ImageVector,
    description: String,
    intent: MainIntent,
    mainViewState: MainViewState,
    modifier: Modifier
) {
    val viewModel: MainViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
    Button(
        onClick = { viewModel.sendIntent(intent) },
        modifier = modifier
            .padding(12.dp),
        colors = buttonTransparentTheme(),
        contentPadding = PaddingValues(0.dp, 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val color by lazy {
                when (state.value.javaClass == mainViewState.javaClass) {
                    true -> Color.Red
                    false -> Color.Black
                }
            }

            Image(
                imageVector = imageVector,
                contentDescription = description,
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(color)
            )
            Text(description, color = color, fontSize = 16.sp)
        }

    }
}