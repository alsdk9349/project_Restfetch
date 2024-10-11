package com.suisei.restfetch.presentation.state

sealed class VerifyEmailState {
    data object WaitRequest : VerifyEmailState()
    data object WaitCode : VerifyEmailState()
    data object WaitVerification : VerifyEmailState()
    data object VerifyComplete : VerifyEmailState()
}