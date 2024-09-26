package com.suisei.restfetch.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suisei.restfetch.presentation.intent.AccountIntent
import com.suisei.restfetch.presentation.state.AccountViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow<AccountViewState>(AccountViewState.Login)
    val viewState: StateFlow<AccountViewState> get() = _viewState

    private val accountIntent = Channel<AccountIntent>()

    init {
        handleViewIntent()
    }

    private fun handleViewIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            accountIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    AccountIntent.LoadLogin -> loadLogin()
                    AccountIntent.LoadSignUp -> loadSignUp()
                    AccountIntent.LoadForgotPassword -> loadForgotPassword()
                }
            }
        }
    }

    private fun loadLogin() {
        _viewState.value = AccountViewState.Login
    }

    private fun loadSignUp() {
        _viewState.value = AccountViewState.SignUp
    }

    private fun loadForgotPassword() {
        _viewState.value = AccountViewState.ForgotPassword
    }

}