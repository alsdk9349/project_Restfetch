package com.suisei.restfetch.data.remote

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.suisei.restfetch.data.model.Report
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources.createFactory
import javax.inject.Inject


class SSEClient @Inject constructor() {

    private var client = ServerClient.okHttpClient

    fun startSse(eventSourceListener: EventSourceListener) {

        val request = Request.Builder()
            .url("http://j11c209.p.ssafy.io:8080/connect")
            .build()

        val factory = createFactory(client)
        val eventSource = factory.newEventSource(request, eventSourceListener)
    }
}