package com.suisei.restfetch.data.model

data class RegisterObserver(

    val observerSerialNumber: String,
    val fetchSerialNumber: String,
    val latitude: Double,
    val longitude: Double,
    val location: String
    /*body["observerSerialNumber"] = observerSerialNumber
body["fetchSerialNumber"] = fetchSerialNumber
body["latitude"] = 0.0
body["longitude"] = 0.0
body["location"] = nickname*/
)
