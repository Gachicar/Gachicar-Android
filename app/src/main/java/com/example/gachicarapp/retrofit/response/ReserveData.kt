package com.example.gachicarapp.retrofit.response

data class ReserveData(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val car: Car,
        val departure: Any,
        val destination: String,
        val driveTime: Any,
        val endTime: String,
        val startTime: Any,
        val userName: String
    ) {
        data class Car(
            val carName: String,
            val carNumber: String,
            val curLoc: String,
            val driveTime: Int,
            val latestDate: String,
            val location: String
        )
    }
}