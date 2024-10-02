package com.suisei.restfetch.data.model.response

data class LoginResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: MemberData
)

data class MemberData(
    val memberId: Int,
    val nickname: String,
    val email: String
)
