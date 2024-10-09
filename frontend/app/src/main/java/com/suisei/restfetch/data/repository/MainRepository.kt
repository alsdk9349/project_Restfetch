package com.suisei.restfetch.data.repository

import com.suisei.restfetch.data.model.Observer
import com.suisei.restfetch.data.model.Report
import com.suisei.restfetch.presentation.state.HomeViewState
import com.suisei.restfetch.presentation.state.MainViewState
import com.suisei.restfetch.presentation.state.MyPageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainRepository @Inject constructor() {

    private val _crtObserver = MutableStateFlow(Observer())
    val crtObserver: StateFlow<Observer> = _crtObserver

    private val _selectedReport = MutableStateFlow(Report())
    val selectedReport: StateFlow<Report> = _selectedReport

    private val _state: MutableStateFlow<MainViewState> = MutableStateFlow(MainViewState.MyPage(MyPageState()))
    val state: StateFlow<MainViewState> get() = _state

    var lastHomeViewState: HomeViewState
    var lastMyPageState: MyPageState

    private val _newReports: MutableStateFlow<List<Report>> = MutableStateFlow(ArrayList())
    val newReports: StateFlow<List<Report>> = _newReports

    private val _notify = MutableStateFlow(false)
    val notify: StateFlow<Boolean> = _notify

    private val _moveReportIndex = MutableStateFlow(-1)
    val moveReportIndex: StateFlow<Int> = _moveReportIndex

    init {
        val crtMyPageViewState = _state.value as MainViewState.MyPage
        lastMyPageState = crtMyPageViewState.myPageState

        _state.value = MainViewState.Home(HomeViewState())
        val crtHomeViewState = _state.value as MainViewState.Home
        lastHomeViewState = crtHomeViewState.homeState
    }
    fun selectFallen(item: Report) {
        _selectedReport.value = item
    }

    fun changeObserver(observer: Observer) {
        _crtObserver.value = observer
    }

    fun updateState(viewState: MainViewState) {
        _state.value = viewState
    }

    fun addNewReport(report: Report) {
        val updatedList = _newReports.value.toMutableList()
        updatedList.add(report)
        _newReports.value = updatedList
    }

    fun resetNewReports() {
         _newReports.value = ArrayList()

    }

    fun setMoveReportIndex(index: Int) {
        _moveReportIndex.value = index
    }

    fun setNotify(state: Boolean) {
        _notify.value = state
    }
}