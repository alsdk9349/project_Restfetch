package com.suisei.restfetch.data.model

data class RequestResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val data: T
)