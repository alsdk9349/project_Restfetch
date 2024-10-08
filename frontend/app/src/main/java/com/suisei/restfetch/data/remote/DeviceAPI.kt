package com.suisei.restfetch.data.remote

import com.suisei.restfetch.data.model.Fetcher
import com.suisei.restfetch.data.model.Observer
import com.suisei.restfetch.data.model.RegisterObserver
import com.suisei.restfetch.data.model.Report
import com.suisei.restfetch.data.model.RequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceAPI {
    companion object {
        private const val FETCH_BASE = "/fetch"
        private const val OBSERVER_BASE = "/observer"
        private const val PICK_BASE = "/pick"
        val GetFetcherSuccess = "S008"
    }

    @POST("$FETCH_BASE/register")
    suspend fun registerFetcher(@Body body: Map<String, String>): Response<RequestResponse<Fetcher>>

    @POST("$FETCH_BASE/{fetcherId}")
    suspend fun deleteFetcher(@Path("fetcherId") fetcherId: Long): Response<RequestResponse<Any>>

    @GET("$FETCH_BASE/get")
    suspend fun getFetcherList(): Response<RequestResponse<List<Fetcher>>>

    @POST("$OBSERVER_BASE/register")
    suspend fun registerObserver(@Body body: RegisterObserver): Response<RequestResponse<Observer>>

    @GET("$FETCH_BASE/{fetcherId}/observerList")
    suspend fun getObserverList(@Path("fetcherId") fetcherId: Long): Response<RequestResponse<List<Observer>>>

    @GET("$OBSERVER_BASE/{observerId}/reports")
    suspend fun getReportList(@Path("observerId") observerId: Long): Response<RequestResponse<List<Report>>>

    @POST("$PICK_BASE/request/{reportId}")
    suspend fun requestPick(@Path("reportId") reportId: Long): Response<RequestResponse<Any>>

    @POST("$PICK_BASE/check/{reportId}")
    suspend fun checkPickComplete(@Path("reportId") reportId: Long): Response<RequestResponse<Any>>
}