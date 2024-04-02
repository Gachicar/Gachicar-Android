package com.example.gachicarapp.retrofit

import android.content.Context
import com.example.gachicarapp.BuildConfig.SERVER_IP_ADDRESS
import com.example.gachicarapp.retrofit.service.LoginService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConnection {
    //객체를 하나만 생성하는 싱글턴 패턴을 적용합니다.
    companion object {
        //API 서버의 주소가 BASE_URL이 됩니다.
        private val SERVER_IP = SERVER_IP_ADDRESS
        private val BASE_URL = "http://${SERVER_IP}:9090"

        private var INSTANCE: Retrofit? = null
        private var instanceWithoutToken: Retrofit? = null

        /**
         * 액세스 토큰과 함께 API 요청
         */
        fun getInstance(context: Context): Retrofit {
            if (INSTANCE == null) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY // HTTP 요청/응답 내용을 로그로 기록
                }
                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
                    .addInterceptor(TokenInterceptor(context, createLoginService())) // TokenInterceptor 추가
                    .build()

                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()
            }
            return INSTANCE!!
        }


        /**
         * 액세스 토큰 없이 API 요청
         * - 회원가입/로그인
         * - 리프레시 토큰으로 액세스 토큰 갱신할 때
         */
        fun getInstanceWithoutToken(context: Context): Retrofit {
            if (instanceWithoutToken == null) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY // HTTP 요청/응답 내용을 로그로 기록
                }
                val httpClient = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
                    .build()

                instanceWithoutToken = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()
            }
            return instanceWithoutToken!!
        }

        private fun createHttpClientWithToken(context: Context): OkHttpClient {
            val loginService = createLoginService()
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(TokenInterceptor(context, loginService)) // TokenInterceptor 추가
            return httpClient.build()
        }

        private fun createHttpClientWithoutToken(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        fun createLoginService(): LoginService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // HTTP 요청/응답 내용을 로그로 기록
            }
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // 로깅 인터셉터 추가
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
                .create(LoginService::class.java)
        }
    }
}