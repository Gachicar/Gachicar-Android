package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.response.getNewToken
import com.example.gachicarapp.retrofit.response.socialSignUpandLogin
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {
    // 로그인 관련 API
    @POST("/api/auth/social")
    fun token(@Header("Authorization") accessToken: String): Call<socialSignUpandLogin>

    // 리프레시 토큰으로 액세스 토큰 갱신
    @POST("/api/auth/newToken")
    fun requestNewAccessToken(@Header("Authorization") refreshToken: String): Call<getNewToken>
}