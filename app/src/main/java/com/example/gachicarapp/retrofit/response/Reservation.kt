package com.example.gachicarapp.retrofit.response

class Reservation(
    val userName: String,
    val driveTime: Int?, // Nullable로 설정하여 null 값 처리 가능
    val startTime: String,
    val endTime: String?,
    val departure: String?,
    val destination: String
)
