package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.CarAndReport
import com.example.gachicarapp.retrofit.response.MostUser
import retrofit2.Call
import retrofit2.http.GET

interface ReportService {

    @GET("/api/report/most")
    fun getMostUserInGroup(): Call<ApiResponse<MostUser>>

    // 특정 사용자의 전체 예약 내역 조회
    @GET("/api/report/reserve/user")
    fun getAllReservations(): Call<ApiResponse<CarAndReport>>

    // 사용자가 속한 그룹의 전체 예약 내역 조회
    @GET("/api/report/reserve/group")
    fun getReserveReports(): Call<ApiResponse<CarAndReport>>


//    @GET("/api/report/usage")
//    fun getUsersUsageCounts(): Call<ApiResponse<>>
}