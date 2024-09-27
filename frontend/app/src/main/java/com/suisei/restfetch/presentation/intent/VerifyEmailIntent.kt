package com.suisei.restfetch.presentation.intent

sealed interface VerifyEmailIntent {
    data object LoadRequestResendButton : VerifyEmailIntent
    data object LoadRequestVerifyButton : VerifyEmailIntent
    data object LoadVerifyComplete : VerifyEmailIntent
}