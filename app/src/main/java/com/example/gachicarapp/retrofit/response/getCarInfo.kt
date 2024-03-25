package com.example.gachicarapp.retrofit.response

data class getCarInfo(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val carName: String,
        val carNumber: String,
        val curLoc: String,
        val driveTime: Int,
        val latestDate: String,
        val location: String
    )
}