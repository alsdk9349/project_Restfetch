package com.suisei.restfetch.presentation.state

sealed class MainViewState {
    data class Home(val homeState: HomeViewState) : MainViewState()
    data class MyPage(val myPageState: MyPageState) : MainViewState()

    companion object {
        fun default() = Home(HomeViewState())
    }
}

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

data class MyPageState(
    val qrScanState: Boolean = false
)