package com.suisei.restfetch.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class NotifyRepository @Inject constructor() {
    private val _notifyState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val notifyState: StateFlow<Boolean> = _notifyState
    private val _notifyMessage: MutableStateFlow<String> = MutableStateFlow("")
    val notifyMessage: StateFlow<String> = _notifyMessage

    fun showNotify(message: String) {
        _notifyMessage.value = message
        _notifyState.value = true
    }

    fun closeNotify() {
        _notifyState.value = false
    }
}