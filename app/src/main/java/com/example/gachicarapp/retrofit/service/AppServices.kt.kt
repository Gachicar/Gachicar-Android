package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.ReserveData
import com.example.gachicarapp.retrofit.response.getCarInfo
import com.example.gachicarapp.retrofit.response.statistic_r_Response
import retrofit2.Call
import retrofit2.http.*


interface AppServices {

    // 예약 데이터 API
    @GET("/api/report/reserve")
    fun getReserveData(): Call<ReserveData>

    // 통계 관련 API
    @GET("/api/car/fuel")
    fun getFuelData(): Call<statistic_r_Response>

    @GET("/api/report")
    fun getDriveReport(): Call<DriveReport>
  
}