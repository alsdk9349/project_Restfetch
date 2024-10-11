package com.suisei.restfetch.presentation.intent

sealed interface AccountIntent {
    data object LoadLogin: AccountIntent
    data object LoadSignUp: AccountIntent
    data object LoadForgotPassword: AccountIntent
}