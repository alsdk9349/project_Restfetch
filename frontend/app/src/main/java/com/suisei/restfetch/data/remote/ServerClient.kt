package com.suisei.restfetch.data.remote

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

object ServerClient {
    private const val BASE_URL = "http://j11c209.p.ssafy.io:8080/"

    private var builder = OkHttpClient().newBuilder()
    var okHttpClient = builder
        .cookieJar(JavaNetCookieJar(CookieManager()))
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .connectTimeout(10, TimeUnit.SECONDS) // 연결 타임아웃 설정
        .writeTimeout(10, TimeUnit.SECONDS) // 작성 타임아웃 설정
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