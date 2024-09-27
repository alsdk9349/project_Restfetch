package com.suisei.restfetch.presentation.state

sealed class MainViewState {
    data class Home(val homeViewState: HomeViewState) : MainViewState()
    data object MyPage : MainViewState()

    data class HomeViewState(
        val notificationState: NotificationState = NotificationState(),
        val selectState: SelectState = SelectState()
    )

    data class NotificationState(
        val newNotification: Boolean = false
    )

    data class SelectState(
        var isSelected: Boolean = false
    )
}

