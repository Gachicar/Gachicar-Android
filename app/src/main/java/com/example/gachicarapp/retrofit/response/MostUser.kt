package com.example.gachicarapp.retrofit.response

data class MostUser(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val userId: Int,
        val userName: String
    )
}