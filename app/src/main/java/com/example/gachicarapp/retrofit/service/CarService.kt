package com.example.gachicarapp.retrofit.service

import com.example.gachicarapp.retrofit.request.CreateCar
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.getCarInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CarService {
    // Car 정보 API
    @GET("/api/car")
    fun getCarHomeInfo(): Call<getCarInfo>

    // 공유차량 등록
    @POST("/api/car")
    fun createCar(
        @Body carData: CreateCar
    ): Call<ApiResponse<CreateCar>>

}