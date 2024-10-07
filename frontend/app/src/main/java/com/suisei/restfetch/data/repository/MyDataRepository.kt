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
        //_observerArrayList.add(observer)
        //_observerList.value = _observerArrayList
    }

    fun addObserverList(observerList: List<Observer>) {
        val updatedList = _observerList.value.toMutableList()
        updatedList.addAll(observerList)
        _observerList.value = updatedList
    }

    fun addReportList(pictureList: List<Report>) {
        val updatedList = _reportList.value.toMutableList()
        updatedList.addAll(pictureList)
        _reportList.value = updatedList
    }

    fun updateDeviceList(productList: List<Product>) {
        _productList.value = productList
    }

    fun removeReport(report: Report) {
        val updatedList = reportList.value.toMutableList()
        updatedList.remove(report)
    }
}