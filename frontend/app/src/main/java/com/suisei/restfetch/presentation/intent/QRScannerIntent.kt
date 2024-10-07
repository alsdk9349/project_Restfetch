package com.suisei.restfetch.presentation.intent

interface QRScannerIntent {
    data object ShowQRRegistration : QRScannerIntent
    data object HideQRRegistration : QRScannerIntent
}