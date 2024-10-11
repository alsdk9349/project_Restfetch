package com.suisei.restfetch.data.model

data class Observer(
    val observerId: Long = 0,
    val observerSerialNumber: String = "",
    val location: String = "전체",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
