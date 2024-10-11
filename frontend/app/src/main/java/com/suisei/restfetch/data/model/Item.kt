package com.suisei.restfetch.data.model

import java.time.LocalDateTime

data class Item(
    val reportId: Long = 0,
    val observerId: Long = 0,
    val observerSerialNumber: String = "",
    val picture: String = "",
    val name: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val picked: Boolean = false
)
