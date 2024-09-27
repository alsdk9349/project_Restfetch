package com.suisei.restfetch.presentation.intent

sealed interface MainIntent {
    data object LoadHome: MainIntent
    data object LoadMyPage : MainIntent
    data object ShowFetchButton : MainIntent
    data object HideFetchButton : MainIntent
}