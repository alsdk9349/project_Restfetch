package com.suisei.restfetch.presentation.state

sealed class VerifyEmailState {
    data object WaitRequestCode : VerifyEmailState()
    data object WaitEnterCode : VerifyEmailState()
    data object WaitRequestVerify : VerifyEmailState()
    data object VerifyComplete : VerifyEmailState()
}