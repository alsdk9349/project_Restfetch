package com.suisei.restfetch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suisei.restfetch.presentation.intent.AccountIntent
import com.suisei.restfetch.presentation.intent.VerifyEmailIntent
import com.suisei.restfetch.presentation.state.AccountViewState
import com.suisei.restfetch.presentation.state.VerifyEmailState
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

    private val _verifyEmailState = MutableStateFlow<VerifyEmailState>(VerifyEmailState.WaitRequestCode)
    val verifyEmailState: StateFlow<VerifyEmailState> get() = _verifyEmailState
    private val verifyEmailIntent = Channel<VerifyEmailIntent>()

    init {
        handleViewIntent()
        handleVerifyEmailIntent()
    }

    fun sendViewIntent(intent: AccountIntent) = viewModelScope.launch(Dispatchers.IO) {
        accountIntent.send(intent)
    }

    fun sendVerifyEmailIntent(intent: VerifyEmailIntent) = viewModelScope.launch(Dispatchers.IO) {
        verifyEmailIntent.send(intent)
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

    private fun handleVerifyEmailIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            verifyEmailIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    VerifyEmailIntent.LoadRequestResendButton -> loadRequestResendButton()
                    VerifyEmailIntent.LoadRequestVerifyButton -> loadRequestVerifyButton()
                    VerifyEmailIntent.LoadVerifyComplete -> loadCompleteText()
                }
            }
        }
    }

    private fun loadRequestResendButton() {
        _verifyEmailState.value = VerifyEmailState.WaitEnterCode
    }

    private fun loadRequestVerifyButton() {
        _verifyEmailState.value = VerifyEmailState.WaitRequestVerify
    }

    private fun loadCompleteText() {
        _verifyEmailState.value = VerifyEmailState.VerifyComplete
    }

}