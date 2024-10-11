package com.suisei.restfetch.data.repository

import com.suisei.restfetch.data.model.Fetcher
import com.suisei.restfetch.data.model.Observer
import com.suisei.restfetch.data.model.Product
import com.suisei.restfetch.data.model.Report
import com.suisei.restfetch.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MyDataRepository @Inject constructor() {
    private val _userData = MutableStateFlow(User(0, "", ""))
    val userData: StateFlow<User> = _userData

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    private val _fetcherList = MutableStateFlow<List<Fetcher>>(emptyList())
    val fetcherList = _fetcherList.asStateFlow()

    private val _observerList = MutableStateFlow<List<Observer>>(emptyList())
    val observerList: StateFlow<List<Observer>> = _observerList

    private var _observerMap = MutableStateFlow<Map<Long, Observer>>(emptyMap())
    val observerMap: StateFlow<Map<Long, Observer>> = _observerMap.asStateFlow()

    private val _reportList = MutableStateFlow<List<Report>>(emptyList())
    val reportList: StateFlow<List<Report>> = _reportList

    private val _reportIdSet = MutableStateFlow<Set<Long>>(emptySet())
    val reportIdSet = _reportIdSet.asStateFlow()

    private val _waitPickSet = MutableStateFlow<Set<Long>>(emptySet())
    val waitPickSet = _waitPickSet.asStateFlow()

    private val _crtPicking = MutableStateFlow<Long>(-1)
    val crtPicking = _crtPicking.asStateFlow()

    fun setUser(user: User) {
        _userData.value = user
    }

    fun addFetcher(fetcher: Fetcher) {
        val updatedList = _fetcherList.value.toMutableList()
        updatedList.add(fetcher)
        _fetcherList.value = updatedList
    }

    fun updateFetcherList(fetcherList: List<Fetcher>) {
        val updatedList = _fetcherList.value.toMutableList()
        updatedList.addAll(fetcherList)
        _fetcherList.value = updatedList
    }

    fun addObserver(observer: Observer) {
        val updatedList = _observerList.value.toMutableList()
        updatedList.add(observer)
        _observerList.value = updatedList

        val map = _observerMap.value.toMutableMap()
        map[observer.observerId] = observer
        _observerMap.value = map
    }

    fun updateObserverList(observerList: List<Observer>) {
        _observerList.value = observerList

        val map = HashMap<Long, Observer>()
        for(observer in observerList) {
            map[observer.observerId] = observer
        }
        _observerMap.value = map
    }

    fun addNewReport(report: Report) {
        val updatedList = _reportList.value.toMutableList()
        updatedList.add(0, report)
        _reportList.value = updatedList

        val updatedSet = _reportIdSet.value.toMutableSet()
        updatedSet.add(report.reportId)
        _reportIdSet.value = updatedSet
    }

    fun removeReport(reportId: Long) {
        val updatedList = _reportList.value.toMutableList()
        updatedList.removeIf { it.reportId == reportId }
        _reportList.value = updatedList

        val updatedSet = _reportIdSet.value.toMutableSet()
        updatedSet.remove(reportId)
        _reportIdSet.value = updatedSet
    }

    fun updateReportList(reportList: List<Report>) {
        _reportList.value = reportList.reversed()

        _reportIdSet.value = reportList.map { it.reportId }.toSet()
    }

    fun updateDeviceList(productList: List<Product>) {
        _productList.value = productList
    }

    fun addPickRequest(reportId: Long) {
        val updatedSet = _waitPickSet.value.toMutableSet()
        updatedSet.add(reportId)
        _waitPickSet.value = updatedSet
    }

    fun setCrtPicking(id: Long) {
        _crtPicking.value = id
    }
}