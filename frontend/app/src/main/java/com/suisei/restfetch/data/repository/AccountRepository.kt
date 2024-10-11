package com.suisei.restfetch.data.repository

import com.suisei.restfetch.data.model.User
import com.suisei.restfetch.presentation.state.AccountViewState
import com.suisei.restfetch.presentation.state.VerifyEmailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AccountRepository @Inject constructor() {
    private val _viewState = MutableStateFlow<AccountViewState>(AccountViewState.Login)
    val viewState: StateFlow<AccountViewState> get() = _viewState

    private val _verifyState = MutableStateFlow<VerifyEmailState>(VerifyEmailState.WaitRequest)
    val verifyState: StateFlow<VerifyEmailState> get() = _verifyState

    fun updateViewState(viewState: AccountViewState) {
        _viewState.value = viewState
    }

    fun updateVerifyState(verifyState: VerifyEmailState) {
        _verifyState.value = verifyState
    }
}