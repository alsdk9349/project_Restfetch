package com.suisei.restfetch.data.repository

import com.suisei.restfetch.presentation.state.QRScannerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class QRScannerRepository @Inject constructor() {
    private val _scannedText = MutableStateFlow("")
    val scannedText: StateFlow<String> = _scannedText
    private val _isCameraBind = MutableStateFlow(false)
    val isCameraBind: StateFlow<Boolean> = _isCameraBind
    private val _productNickname = MutableStateFlow("")
    val productNickname: StateFlow<String> = _productNickname

    private val _qrScannerState = MutableStateFlow(QRScannerState())
    val qrScannerState: StateFlow<QRScannerState> = _qrScannerState

    fun updateScanningState(state: Boolean) {
        _qrScannerState.value = _qrScannerState.value.copy(scanning = state, qrScanState = _qrScannerState.value.qrScanState)
    }

    fun updateScanState(state: Boolean) {
        _qrScannerState.value.qrScanState = state
    }

    fun updateScannedText(text: String) {
        _scannedText.value = text
    }

    fun updateProductNickname(nickname: String) {
        _productNickname.value = nickname
    }
}