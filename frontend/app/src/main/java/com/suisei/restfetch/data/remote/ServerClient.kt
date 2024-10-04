package com.suisei.restfetch.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerClient {
    private const val BASE_URL = "http://j11c209.p.ssafy.io:8080/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userRetrofit: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }
}