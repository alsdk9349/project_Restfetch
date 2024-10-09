package com.suisei.restfetch.data.repository

import android.util.Log
import com.suisei.restfetch.data.model.Fetcher
import com.suisei.restfetch.data.model.Observer
import com.suisei.restfetch.data.model.Product
import com.suisei.restfetch.data.model.Report
import com.suisei.restfetch.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MyDataRepository @Inject constructor() {
    private val _userData = MutableStateFlow(User(0, "", ""))
    val userData: StateFlow<User> = _userData

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    private val _fetcherList = MutableStateFlow<List<Fetcher>>(ArrayList())
    val fetcherList: StateFlow<List<Fetcher>> = _fetcherList

    private val _observerList = MutableStateFlow<List<Observer>>(ArrayList())
    val observerList: StateFlow<List<Observer>> = _observerList

    private var _observerMap: HashMap<Long, Observer> = HashMap()
    val observerMap = _observerMap

    private val _reportList = MutableStateFlow<List<Report>>(ArrayList())
    val reportList: StateFlow<List<Report>> = _reportList

    fun setUser(user: User) {
        _userData.value = user
    }

    fun addFetcher(fetcher: Fetcher) {
        //_fetcherArrayList.add(fetcher)
        //_fetcherList.value = _fetcherArrayList
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
        _observerMap[observer.observerId] = observer
    }

    fun updateObserverList(observerList: List<Observer>) {
        _observerList.value = observerList

        _observerMap = HashMap()
        for(observer in observerList) {
            _observerMap[observer.observerId] = observer
        }
    }

    fun addReportList(pictureList: List<Report>) {
        val updatedList = _reportList.value.toMutableList()
        updatedList.addAll(pictureList)
        _reportList.value = updatedList
    }

    fun updateReportList(reportList: List<Report>) {
        _reportList.value = reportList
    }

    fun updateDeviceList(productList: List<Product>) {
        _productList.value = productList
    }

    fun removeReport(report: Report) {
        val updatedList = reportList.value.toMutableList()
        updatedList.remove(report)
        _reportList.value = updatedList
    }

    fun addNewReport(report: Report) {
        val updatedList = _reportList.value.toMutableList()
        updatedList.add(0, report)
        _reportList.value = updatedList
    }
}