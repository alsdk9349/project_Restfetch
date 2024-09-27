package com.suisei.restfetch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suisei.restfetch.presentation.intent.MainIntent
import com.suisei.restfetch.presentation.state.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<MainViewState>(MainViewState.Home(MainViewState.HomeViewState()))
    val state: StateFlow<MainViewState> get() = _state

    var lastHomeViewState: MainViewState.HomeViewState

    private val mainIntent = Channel<MainIntent> ()

    init {
        val currentState = _state.value as MainViewState.Home
        lastHomeViewState = currentState.homeViewState

        handleViewIntent()
    }

    fun sendIntent(intent: MainIntent) = viewModelScope.launch(Dispatchers.IO) {
        mainIntent.send(intent)
    }

    private fun handleViewIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            mainIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    MainIntent.LoadHome -> loadHome()
                    MainIntent.LoadMyPage -> loadMyPage()
                    MainIntent.ShowFetchButton -> showFetchButton()
                    MainIntent.HideFetchButton -> hideFetchButton()
                }
            }
        }
    }

    private fun loadHome() {
        _state.value = MainViewState.Home(lastHomeViewState)
    }

    private fun loadMyPage() {
        _state.value = MainViewState.MyPage
    }

    private fun showFetchButton() {
        showFetchButton(true)
    }

    private fun hideFetchButton() {
        showFetchButton(false)
    }

    private fun showFetchButton(isSelected: Boolean) {
        val currentState = _state.value as MainViewState.Home

        val newState = currentState.copy(
            homeViewState = currentState.homeViewState.copy(
                selectState = currentState.homeViewState.selectState.copy(
                    isSelected = isSelected
                )
            )
        )

        _state.value = newState
        lastHomeViewState = newState.homeViewState
    }
}