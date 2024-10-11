package com.suisei.restfetch.presentation.viewmodel

import android.graphics.Bitmap.createScaledBitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.suisei.restfetch.data.model.Observer
import com.suisei.restfetch.data.model.Report
import com.suisei.restfetch.data.remote.SSEClient
import com.suisei.restfetch.data.remote.ServerClient
import com.suisei.restfetch.data.repository.MainRepository
import com.suisei.restfetch.data.repository.MyDataRepository
import com.suisei.restfetch.data.repository.NotifyRepository
import com.suisei.restfetch.presentation.intent.MainIntent
import com.suisei.restfetch.presentation.state.HomeViewState
import com.suisei.restfetch.presentation.state.MainViewState
import com.suisei.restfetch.presentation.state.MyPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val myDataRepository: MyDataRepository,
    private val notifyRepository: NotifyRepository,
    private val sseClient: SSEClient
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

    val observerMap = myDataRepository.observerMap
    val newReports = repository.newReports
    val notifyState = repository.newReportNotify

    val moveReportIndex = repository.moveReportIndex

    val crtPicking = myDataRepository.crtPicking
    val waitPickSet = myDataRepository.waitPickSet

    init {

        handleIntent()

        updateFetcherList()
        observeFetchers()
        observeObservers()

        val eventSourceListener = object: EventSourceListener() {
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                val jsonObject = JsonParser.parseString(data).asJsonObject
                val report = Gson().fromJson(jsonObject, Report::class.java)
                Log.e("TEST", report.toString())
                if(!myDataRepository.reportIdSet.value.contains(report.reportId)) {
                    if(report.picture != null) {
                        myDataRepository.addNewReport(report)
                        repository.addNewReport(report)
                        repository.increaseNewReportCount()
                        Log.e("TEST", "새로운 이미지 발견")
                    }
                } else {

                    if(myDataRepository.crtPicking.value != report.reportId && !report.picked) {
                        myDataRepository.setCrtPicking(report.reportId)
                    } else if(myDataRepository.crtPicking.value == report.reportId && report.picked) {
                        myDataRepository.removeReport(report.reportId)
                        myDataRepository.setCrtPicking(-1)
                    }
                    /*if(type == "물건 회수 시작했습니다") {
                        myDataRepository.setCrtPicking(report.reportId)
                    } else if(type == "물건을 회수했습니다.") {
                        myDataRepository.removeReport(report.reportId)
                        myDataRepository.setCrtPicking(-1)
                    }*/
                    /*if(myDataRepository.crtPicking.value != report.reportId) {
                        myDataRepository.setCrtPicking(report.reportId)
                        Log.e("TEST", "회수 시작")
                    } else {
                        myDataRepository.removeReport(report.reportId)
                        myDataRepository.setCrtPicking(-1)
                        Log.e("TEST", "회수 종료")
                    }*/
                }

                super.onEvent(eventSource, id, type, data)
            }

            override fun onClosed(eventSource: EventSource) {
                sseClient.startSse(this)
                super.onClosed(eventSource)
            }
        }
        sseClient.startSse(eventSourceListener)
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
    }

    private fun loadMyPage() {
        repository.updateState(MainViewState.MyPage(lastMyPageState))
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
            myDataRepository.addObserver(Observer())

            val list = ArrayList<Observer>()
            list.add(Observer())
            for (fetcher in fetcherList.value) {

                val observerResponse = retrofit.getObserverList(fetcher.fetchId)
                if (observerResponse.isSuccessful) {
                    val data = observerResponse.body()?.data
                    list.addAll(data!!)
                }
            }

            myDataRepository.updateObserverList(list)
        }
    }

    private fun updateReportList() {
        CoroutineScope(Dispatchers.IO).launch {
            val reportList: ArrayList<Report> = ArrayList()
            for (observer in observerList.value) {
                val response = retrofit.getReportList(observer.observerId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val observerReportList = response.body()!!.data
                        reportList.addAll(observerReportList)

                    }
                }
            }

            myDataRepository.updateReportList(reportList)
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

    fun stringToImageBitmap(image: String): ImageBitmap? {
        val encodedByte: ByteArray = Base64.decode(image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.size)
        if(bitmap == null) {
            return null
        }
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

    fun requestPick() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.requestPick(selectedReport.value.reportId)
            if(response.isSuccessful) {
                myDataRepository.addPickRequest(selectedReport.value.reportId)
                notifyRepository.showNotify(response.body()!!.message)
            } else {
                handleResponseError(response.errorBody()!!)
            }
        }
    }

    fun changeObserver(observer: Observer) {
        repository.changeObserver(observer)
    }

    fun addNewReport(report: Report) {
        repository.addNewReport(report)
    }

    fun resetNewReports() {
        repository.resetNewReports()
    }

    fun hideNewNotify() {
        repository.resetNewReportCount()
    }

    fun moveToReport(index: Int) {
        repository.setMoveReportIndex(index)
    }

    private fun handleResponseError(errorResponseBody: ResponseBody) {
        val error = JsonParser.parseString(errorResponseBody.string()).asJsonObject
        notifyRepository.showNotify(error.get("message").asString)
    }
}