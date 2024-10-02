package com.suisei.restfetch.data.model.response

data class RequestResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: String
)