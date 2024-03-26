package com.example.gachicarapp.retrofit.response

class ApiResponse<T>(
        val code: Int,
        val data: T,
        val message: String
)
