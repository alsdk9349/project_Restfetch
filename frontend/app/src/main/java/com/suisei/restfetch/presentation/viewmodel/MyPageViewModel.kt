package com.suisei.restfetch.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.JsonParser
import com.suisei.restfetch.data.model.RegisterObserver
import com.suisei.restfetch.data.remote.ServerClient
import com.suisei.restfetch.data.repository.MyDataRepository
import com.suisei.restfetch.data.repository.NotifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myDataRepository: MyDataRepository,
    private val notifyRepository: NotifyRepository
) :
    ViewModel() {
    val userData = myDataRepository.userData
    val reportList = myDataRepository.reportList
    private val deviceAPI = ServerClient.deviceAPI

    fun addFetcher(serialNumber: String, nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val body: HashMap<String, String> = HashMap()
            body["fetchSerialNumber"] = serialNumber
            body["nickname"] = nickname

            val response = deviceAPI.registerFetcher(body)
            if (response.isSuccessful) {
                Log.e("TEST", body.toString())
                myDataRepository.addFetcher(response.body()!!.data)
                notifyRepository.showNotify(response.body()!!.message)
            } else {
                handleResponseError(response.errorBody()!!)
            }
        }
    }

    fun addObserver(observerSerialNumber: String, fetchSerialNumber: String, nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val body = RegisterObserver(
                observerSerialNumber,
                fetchSerialNumber,
                latitude = 6.735,
                longitude = 1.434,
                nickname
            )

            val response = deviceAPI.registerObserver(body)
            if (response.isSuccessful) {
                Log.e("TEST", body.toString())
                myDataRepository.addObserver(response.body()!!.data)
                notifyRepository.showNotify(response.body()!!.message)
            } else {
                handleResponseError(response.errorBody()!!)
            }
        }
    }

    private fun handleResponseError(errorResponseBody: ResponseBody) {
        val error = JsonParser.parseString(errorResponseBody.string()).asJsonObject
        notifyRepository.showNotify(error.get("message").asString)
    }
}

