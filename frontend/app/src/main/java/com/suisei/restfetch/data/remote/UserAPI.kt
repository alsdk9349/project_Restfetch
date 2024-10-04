package com.suisei.restfetch.data.remote

import com.suisei.restfetch.data.model.response.LoginResponse
import com.suisei.restfetch.data.model.response.RequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    companion object {
        private const val BASE = "user"
        val LoginSuccess get() = "S001"
        val CreateSuccess get() = "S002"
        val SendSuccess get() = "S003"
        val VerifySuccess get() = "S004"
        val LogoutSuccess get() = "S005"
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