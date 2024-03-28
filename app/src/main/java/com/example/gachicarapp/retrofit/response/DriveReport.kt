package com.example.gachicarapp.retrofit.response

data class DriveReport(
    val car: Car,
    val departure: String,
    val destination: String,
    val driveTime: Int,
    val endTime: String,
    val startTime: String,
    val userName: String
)
