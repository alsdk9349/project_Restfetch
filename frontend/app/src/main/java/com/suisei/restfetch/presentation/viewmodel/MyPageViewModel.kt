package com.suisei.restfetch.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.suisei.restfetch.data.model.RegisterObserver
import com.suisei.restfetch.data.remote.ServerClient
import com.suisei.restfetch.data.repository.MyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val myDataRepository: MyDataRepository) :
    ViewModel() {
    val userData = myDataRepository.userData

    private val deviceAPI = ServerClient.deviceAPI

    fun addFetcher(serialNumber: String, nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val body: HashMap<String, String> = HashMap()
            body["fetchSerialNumber"] = serialNumber
            body["nickname"] = nickname
            val response = deviceAPI.registerFetcher(body)
            if(response.isSuccessful) {
                Log.e("TEST", response.body().toString())
            } else {
                Log.e("TEST", response.errorBody()!!.string())
            }
        }
    }

    fun addObserver(observerSerialNumber: String, fetchSerialNumber: String, nickname: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val body = RegisterObserver(observerSerialNumber, fetchSerialNumber, latitude = 0.0, longitude = 0.0, nickname)

            val response = deviceAPI.registerObserver(body)
            if(response.isSuccessful) {
                Log.e("TEST", response.body().toString())
            } else {
                Log.e("TEST", response.errorBody()!!.string())
            }
        }

    }
}

