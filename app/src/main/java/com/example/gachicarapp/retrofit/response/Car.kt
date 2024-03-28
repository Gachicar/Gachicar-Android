package com.example.gachicarapp.retrofit.response

data class Car (
    val carName: String,
    val carNumber: String,
    val totalDistance: Long,
    val curLoc: String,
    val driveTime: Int,
    val latestDate: String,
    val location: String
)