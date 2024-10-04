package com.suisei.restfetch.data.repository

import com.suisei.restfetch.data.model.Product
import com.suisei.restfetch.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MyDataRepository @Inject constructor() {
    private val _userData = MutableStateFlow(User(0, "", ""))
    val userData: StateFlow<User> = _userData

    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    fun updateUserData(user: User) {
        _userData.value = user
    }

    fun updateDeviceList(productList: List<Product>) {
        _productList.value = productList
    }
}