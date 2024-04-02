package com.example.gachicarapp.retrofit.response

data class ReservationNotification(
    val userName: String,
    val driveTime: Long?,
    val startTime: String,
    val endTime: String?,
    val departure: String?,
    val destination: String
)
