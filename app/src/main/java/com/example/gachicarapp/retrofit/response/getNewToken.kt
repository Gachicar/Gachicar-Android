package com.example.gachicarapp.retrofit.response

data class getNewToken(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val accessToken: String
    )
}