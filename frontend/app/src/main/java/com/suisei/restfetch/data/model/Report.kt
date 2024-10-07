package com.suisei.restfetch.data.model

import java.time.LocalDateTime

data class Report(
    val reportId: Long = 0,
    val observerId: Long = 0,
    val observerSerialNumber: String = "",
    val picture: String = "",
    val createAt: LocalDateTime = LocalDateTime.now(),
    val picked: Boolean = false
)
