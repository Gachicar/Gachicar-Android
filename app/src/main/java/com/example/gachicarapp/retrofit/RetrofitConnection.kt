package com.example.gachicarapp.retrofit

import android.content.Context
import com.example.gachicarapp.retrofit.service.LoginService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConnection {
    //객체를 하나만 생성하는 싱글턴 패턴을 적용합니다.
    companion object {
        //API 서버의 주소가 BASE_URL이 됩니다.
        private const val BASE_URL = "http://localhost:9090"
        private var INSTANCE: Retrofit? = null

        fun getInstance(context: Context): Retrofit {
            if (INSTANCE == null) {
                val httpClient = OkHttpClient.Builder()
                val loginService = createLoginService(context)
                httpClient.addInterceptor(TokenInterceptor(context, loginService)) // Interceptor에 Context와 LoginService 전달

                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
            return INSTANCE!!
        }

        private fun createLoginService(context: Context): LoginService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
                .create(LoginService::class.java)
        }
    }
}