package com.suisei.restfetch.presentation.viewmodel

import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suisei.restfetch.data.model.Observer
import com.suisei.restfetch.data.model.Report
import com.suisei.restfetch.data.remote.ServerClient
import com.suisei.restfetch.data.repository.MainRepository
import com.suisei.restfetch.data.repository.MyDataRepository
import com.suisei.restfetch.presentation.intent.MainIntent
import com.suisei.restfetch.presentation.state.HomeViewState
import com.suisei.restfetch.presentation.state.MainViewState
import com.suisei.restfetch.presentation.state.MyPageState
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
    private val repository: MainRepository,
    private val myDataRepository: MyDataRepository
) : ViewModel() {

    val state = repository.state
    val crtLocation = repository.crtObserver

    var lastHomeViewState: HomeViewState = repository.lastHomeViewState
    var lastMyPageState: MyPageState = repository.lastMyPageState

    val fetcherList = myDataRepository.fetcherList
    val observerList = myDataRepository.observerList
    val reportList = myDataRepository.reportList

    val selectedReport: StateFlow<Report> = repository.selectedReport

    private val mainIntent = Channel<MainIntent> ()

    private val retrofit = ServerClient.deviceAPI

    init {

        handleIntent()

        updateFetcherList()
        observeFetchers()
        observeObservers()
    }

    fun sendIntent(intent: MainIntent) = viewModelScope.launch(Dispatchers.IO) {
        mainIntent.send(intent)
    }

    private fun handleIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            mainIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    MainIntent.LoadHome -> loadHome()
                    MainIntent.LoadMyPage -> loadMyPage()
                    MainIntent.ShowFetchButton -> setFetchButtonVisibility(true)
                    MainIntent.HideFetchButton -> setFetchButtonVisibility(false)
                }
            }
        }
    }

    private fun loadHome() {
        repository.updateState(MainViewState.Home(lastHomeViewState))
        //_state.value =
    }

    private fun loadMyPage() {
        repository.updateState(MainViewState.MyPage(lastMyPageState))
        //_state.value =
    }

    private fun setFetchButtonVisibility(visibility: Boolean) {
        val currentState = state.value as MainViewState.Home

        val newState = currentState.copy(
            homeState = currentState.homeState.copy(
                selectState = currentState.homeState.selectState.copy(
                    isSelected = visibility
                )
            )
        )
        repository.updateState(newState)
        lastHomeViewState = newState.homeState
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

    fun stringToImageBitmap(image: String): ImageBitmap {
        val encodedByte: ByteArray = Base64.decode(image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.size)
        val imageBitmap = createScaledBitmap(bitmap, 800, 450, true).asImageBitmap()

        return imageBitmap
    }

    fun selectReport(item: Report) {
        if (selectedReport.value == item) {
            repository.selectFallen(Report())
            sendIntent(MainIntent.HideFetchButton)
        } else {
            repository.selectFallen(item)
            sendIntent(MainIntent.ShowFetchButton)
        }

    }
}