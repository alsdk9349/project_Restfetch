package com.suisei.restfetch.presentation.intent

sealed interface VerifyEmailIntent {
    data object LoadResendButton : VerifyEmailIntent
    data object LoadVerifyButton : VerifyEmailIntent
    data object LoadCompleteText : VerifyEmailIntent
}