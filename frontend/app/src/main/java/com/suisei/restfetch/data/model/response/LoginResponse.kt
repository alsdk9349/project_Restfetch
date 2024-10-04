package com.suisei.restfetch.data.model.response

import com.suisei.restfetch.data.model.User

data class LoginResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: User
)