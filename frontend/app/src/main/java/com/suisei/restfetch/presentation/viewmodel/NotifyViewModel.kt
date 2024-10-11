package com.suisei.restfetch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.suisei.restfetch.data.repository.NotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotifyViewModel @Inject constructor(private val repository: NotifyRepository) :
    ViewModel() {
    val notifyState = repository.notifyState
    val notifyMessage = repository.notifyMessage

    fun closeNotify() {
        repository.closeNotify()
    }
}