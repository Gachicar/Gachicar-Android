package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.MostUser
import retrofit2.Call
import retrofit2.http.GET

interface ReportService {

    @GET("/api/report/most")
    fun getMostUserInGroup(): Call<ApiResponse<MostUser>>
}