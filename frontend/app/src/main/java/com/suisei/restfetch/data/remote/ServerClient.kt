package com.suisei.restfetch.data.remote

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

object ServerClient {
    private const val BASE_URL = "http://j11c209.p.ssafy.io:8080/"

    private var builder = OkHttpClient().newBuilder()
    private var okHttpClient = builder
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val userRetrofit: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }

    val deviceAPI: DeviceAPI by lazy {
        retrofit.create(DeviceAPI::class.java)
    }
}