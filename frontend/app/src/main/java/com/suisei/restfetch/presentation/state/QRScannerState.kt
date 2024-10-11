package com.suisei.restfetch.presentation.state

data class QRScannerState(
    var scanning: Boolean = false,
    var qrScanState: Boolean = false
)
