package com.suisei.restfetch.data.remote

import com.suisei.restfetch.data.model.response.LoginResponse
import com.suisei.restfetch.data.model.response.RequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    companion object {
        private const val BASE = "user"
        val LoginSuccess get() = "M001"
        val CreateSuccess get() = "M002"
        val SendSuccess get() = "M003"
        val VerifySuccess get() = "M004"
    }

    @POST("$BASE/signup")
    suspend fun signup(@Body body: Map<String, String>): Response<RequestResponse>

    @POST("$BASE/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("$BASE/emails/send")
    suspend fun requestCode(@Body body: Map<String, String>): Response<RequestResponse>

    @POST("$BASE/emails/verify")
    suspend fun verifyCode(@Body body: Map<String, String>): Response<RequestResponse>
}