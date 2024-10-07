package com.suisei.restfetch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suisei.restfetch.data.model.Observer
import com.suisei.restfetch.data.remote.ServerClient
import com.suisei.restfetch.data.repository.MyDataRepository
import com.suisei.restfetch.presentation.intent.MainIntent
import com.suisei.restfetch.presentation.state.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myDataRepository: MyDataRepository
) : ViewModel() {
    private val _state = MutableStateFlow<MainViewState>(MainViewState.Home(MainViewState.HomeViewState()))
    val state: StateFlow<MainViewState> get() = _state

    var lastHomeViewState: MainViewState.HomeViewState

    val fetcherList = myDataRepository.fetcherList
    val observerList = myDataRepository.observerList
    val reportList = myDataRepository.reportList

    private val mainIntent = Channel<MainIntent> ()

    private val retrofit = ServerClient.deviceAPI

    init {
        val currentState = _state.value as MainViewState.Home
        lastHomeViewState = currentState.homeViewState

        handleViewIntent()

        updateFetcherList()
        observeFetchers()
        observeObservers()
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


    private fun updateFetcherList() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.getFetcherList()
            if (response.isSuccessful) {
                val fetcherList = response.body()?.data
                myDataRepository.updateFetcherList(fetcherList!!)
            } else {

            }
        }
    }

    private fun updateObserverList() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = ArrayList<Observer>()
            list.add(Observer())
            for (fetcher in fetcherList.value) {
                val observerResponse = retrofit.getObserverList(fetcher.fetchId)
                if (observerResponse.isSuccessful) {
                    val data = observerResponse.body()?.data
                    list.addAll(data!!)
                }
            }

            myDataRepository.addObserverList(list)
        }
    }

    private fun updateReportList() {
        CoroutineScope(Dispatchers.IO).launch {
            for (observer in observerList.value) {
                val response = retrofit.getReportList(observer.observerId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val observerReportList = response.body()!!.data
                        myDataRepository.addReportList(observerReportList)
                    }
                }
            }
        }
    }

    private fun observeFetchers() {
        viewModelScope.launch {
            fetcherList.collect {
                updateObserverList()
            }
        }
    }

    private fun observeObservers() {
        viewModelScope.launch {
            observerList.collect {
                updateReportList()
            }
        }
    }
}