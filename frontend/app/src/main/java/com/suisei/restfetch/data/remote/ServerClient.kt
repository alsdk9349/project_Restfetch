package com.suisei.restfetch.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit

object ServerClient {
    private const val BASE_URL = "http://j11c209.p.ssafy.io:8080/"

    private var builder = OkHttpClient().newBuilder()

    class ErrorHandlingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return try {
                // 요청을 가로채고 서버에 전달
                val request = chain.request()
                chain.proceed(request)
            } catch (e: IOException) {
                e.message?.let { Log.e("TEST", it) }
                // 네트워크 오류 발생 시 처리
                throw IOException("Network error occurred", e)
            } catch (e: Exception) {
                e.message?.let { Log.e("TEST", it) }
                // 기타 예외 처리
                throw Exception("An unexpected error occurred", e)
            }
        }
    }


    var okHttpClient = builder
        .addInterceptor(ErrorHandlingInterceptor())
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