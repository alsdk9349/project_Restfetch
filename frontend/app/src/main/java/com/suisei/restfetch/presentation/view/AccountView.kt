package com.suisei.restfetch.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.suisei.restfetch.presentation.state.AccountViewState
import com.suisei.restfetch.presentation.view.theme.backgroundColor
import com.suisei.restfetch.presentation.viewmodel.AccountViewModel

@Composable
fun AccountScreen() {
    val viewModel: AccountViewModel = viewModel()
    val viewState = viewModel.viewState.collectAsState()

    Column(
        modifier = Modifier.background(backgroundColor())
    ) {
        when (val state = viewState.value) {
            AccountViewState.Login -> LoginScreen()
            AccountViewState.SignUp -> SignUpScreen()
            AccountViewState.ForgotPassword -> ForgotPasswordScreen()
        }
    }
}

@Composable
fun LoginScreen() {

}

@Composable
fun SignUpScreen() {

}

@Composable
fun ForgotPasswordScreen() {

}