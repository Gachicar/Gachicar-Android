package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.UserData
import retrofit2.Call
import retrofit2.http.GET

interface UserService {
    @GET("/api/user")
    fun getUserInfo(): Call<ApiResponse<UserData>>
}