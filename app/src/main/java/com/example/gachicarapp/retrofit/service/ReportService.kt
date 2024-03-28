package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.CarAndReport
import com.example.gachicarapp.retrofit.response.DriveReport
import com.example.gachicarapp.retrofit.response.UserAndCount
import com.example.gachicarapp.retrofit.response.UserData
import com.example.gachicarapp.retrofit.response.UserReportList
import retrofit2.Call
import retrofit2.http.GET

interface ReportService {

    @GET("/api/report")
    fun getDriveReport(): Call<ApiResponse<DriveReport>>

    @GET("/api/report/all")
    fun getUserReports(): Call<ApiResponse<UserReportList>>

    @GET("/api/report/most")
    fun getMostUserInGroup(): Call<ApiResponse<UserAndCount>>

    // 특정 사용자의 전체 예약 내역 조회
    @GET("/api/report/reserve/user")
    fun getAllReservations(): Call<ApiResponse<CarAndReport>>

    // 사용자가 속한 그룹의 전체 예약 내역 조회
    @GET("/api/report/reserve/group")
    fun getReserveReports(): Call<ApiResponse<CarAndReport>>

    @GET("/api/report/usage")
    fun getUsersUsageCounts(): Call<ApiResponse<UserAndCount>>
}