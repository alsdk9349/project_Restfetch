package com.suisei.restfetch.presentation.state

sealed class AccountViewState {
    data object Login : AccountViewState()
    data object SignUp : AccountViewState()
    data object ForgotPassword : AccountViewState()
}