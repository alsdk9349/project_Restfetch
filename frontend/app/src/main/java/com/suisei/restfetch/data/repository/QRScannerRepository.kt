package com.suisei.restfetch.data.repository

import android.util.Log
import com.suisei.restfetch.data.model.Fetcher
import com.suisei.restfetch.presentation.state.QRScannerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class QRScannerRepository @Inject constructor() {
    private val _serialNumber = MutableStateFlow("")
    val serialNumber: StateFlow<String> = _serialNumber
    private val _isCameraBind = MutableStateFlow(false)
    val isCameraBind: StateFlow<Boolean> = _isCameraBind
    private val _parentFetcher = MutableStateFlow(Fetcher(0, "", "Fetcher 선택"))
    val parentFetcher: StateFlow<Fetcher> = _parentFetcher

    private val _productType = MutableStateFlow(0)
    val productType: StateFlow<Int> = _productType
    private val _productNickname = MutableStateFlow("")
    val productNickname: StateFlow<String> = _productNickname

    private val _qrScannerState = MutableStateFlow(QRScannerState())
    val qrScannerState: StateFlow<QRScannerState> = _qrScannerState

    private val _selectFetcherState = MutableStateFlow(false)
    val selectFetcherState: StateFlow<Boolean> = _selectFetcherState

    fun updateScanningState(state: Boolean) {
        _qrScannerState.value = _qrScannerState.value.copy(scanning = state, qrScanState = _qrScannerState.value.qrScanState)
        updateProductType(0)
        updateSerialNumber("")
        updateProductNickname("")
        updateSelectFetcherState(false)
        setParentFetcher(Fetcher())
    }

    fun updateScanState(state: Boolean) {
        if(_qrScannerState.value.qrScanState != state) {
            _qrScannerState.value.qrScanState = state
        }

    }

    fun updateProductType(type: Int) {
        _productType.value = type
    }

    fun updateSerialNumber(text: String) {
        _serialNumber.value = text
    }

    fun updateProductNickname(nickname: String) {
        _productNickname.value = nickname
    }

    fun updateSelectFetcherState(state: Boolean) {
        if(!state) {
            setParentFetcher(Fetcher())
        }
        _selectFetcherState.value = state
    }

    fun setParentFetcher(fetcher: Fetcher) {
        _parentFetcher.value = fetcher
    }
}