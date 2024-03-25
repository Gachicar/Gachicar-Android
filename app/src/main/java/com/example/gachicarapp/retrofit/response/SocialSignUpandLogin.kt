package com.example.gachicarapp.retrofit.response

data class SocialSignUpandLogin(
    val code: Int,
    val `data`: Data,
    val message: String
) {
    data class Data(
        val accessToken: String,
        val refreshToken: String,
        val type: String
    )
}