package com.suisei.restfetch.data.remote

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources.createFactory


class SSEClient {

    private var client = ServerClient.okHttpClient

    fun startSse() {
        val request = Request.Builder()
            .url("http://j11c209.p.ssafy.io:8080/connect")
            .build()

        val factory = createFactory(client)
        val eventSource = factory.newEventSource(request, object : EventSourceListener() {
            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                Log.e("TEST", "onEvent : $data")
                super.onEvent(eventSource, id, type, data)
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                Log.e("TEST", "Failure")
                Log.e("TEST", t!!.message!!)
                Log.e("TEST", eventSource.request().body.toString())
                Log.e("TEST", response!!.isSuccessful.toString())
                Log.e("TEST", response.body!!.string())
                super.onFailure(eventSource, t, response)
            }

            override fun onOpen(eventSource: EventSource, response: Response) {
                // 연결 성공 시 처리
                Log.e("TEST", "connected")
                super.onOpen(eventSource, response)
            }

            override fun onClosed(eventSource: EventSource) {
                Log.e("TEST", "onClosed")
                //super.onClosed(eventSource)
                startSse()
            }
        })
    }

    /*val Tag: String = "SSE-Test"

    private var eventSource: EventSource? = null

    val eventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)
            Log.d(Tag, "Connection Opened")
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
            Log.d(Tag, "Connection Closed")
        }

        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            super.onEvent(eventSource, id, type, data)

            Log.d(Tag, "On Event Received! Data -: $data")
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)
            Log.d(Tag, "On Failure -: ${response?.body}")
        }
    }

    private var sseCall: Call? = null

    fun startSse() {
        Log.e("TEST", "startSse")

        val client = ServerClient.okHttpClient

        val request = Request.Builder()
            .url("http://j11c209.p.ssafy.io:8080/connect")
            .build()

        sseCall = client.newCall(request)

        sseCall?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TEST", "onFailure " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e("TEST", response.isSuccessful.toString())
                Log.e("TEST", response.toString())
                response.use {
                    if (response.isSuccessful) {
                        val reader = response.body?.charStream()
                        reader?.forEachLine { line ->
                            handleEvent(line)
                        }
                    } else {

                    }
                }
            }
        })
    }

    fun stopSse() {
        sseCall?.cancel()  // 연결 종료
        println("SSE connection closed")
    }

    private fun handleEvent(event: String) {
        // 이벤트 처리 로직
        //println("Received event: $event")
        //Log.e("TEST", JsonParser.parseString(event).asString)

        Log.e("TEST", "Received event: $event")
        //requestData(reportId)
    }*/
}