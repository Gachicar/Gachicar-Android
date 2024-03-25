package com.example.gachicarapp.retrofit.response

data class DriveReport(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val car: Car,
        val departure: String,
        val destination: String,
        val driveTime: Int,
        val endTime: String,
        val startTime: String,
        val userName: String
    ) {
        data class Car(
            val carName: String,
            val carNumber: String
        )
    }
}