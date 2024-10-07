package com.suisei.restfetch.presentation.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.suisei.restfetch.presentation.view.account.AccountScreen
import com.suisei.restfetch.presentation.view.main.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Notify()

    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            AccountScreen(navController)
        }
        composable("main_screen") {
            MainScreen(navController)
        }
    }
}