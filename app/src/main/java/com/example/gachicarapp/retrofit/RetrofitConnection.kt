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
        private const val BASE_URL = "http://172.30.1.5:9090"
        private var INSTANCE: Retrofit? = null
        private var instanceWithoutToken: Retrofit? = null

        /**
         * 액세스 토큰과 함께 API 요청
         */
        fun getInstance(context: Context): Retrofit {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createHttpClientWithToken(context))
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
                instanceWithoutToken = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createHttpClientWithoutToken())
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
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
                .create(LoginService::class.java)
        }
    }
}