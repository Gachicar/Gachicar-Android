package com.example.gachicarapp.retrofit

import android.content.Context
import com.example.gachicarapp.retrofit.service.LoginService
import okhttp3.Interceptor
import okhttp3.Response


class TokenInterceptor(private val context: Context, private val loginService: LoginService) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
//        val response = chain.proceed(request)

        val accessToken = getAccessTokenFromSharedPreferences()

        // 기존 요청에 토큰 추가
        val requestWithToken = if (accessToken != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            request
        }

        // 기존 요청 처리 후 응답 반환
        val response = chain.proceed(requestWithToken)

        // 응답 코드가 401(Unauthorized)이고 토큰이 만료되었다는 메시지를 받으면
        if (response.code == 401 && response.message == "토큰이 만료되었습니다.") {
            // 새로운 액세스 토큰 요청
            val refreshToken = getRefreshTokenFromSharedPreferences()
            if (refreshToken != null) {
                val newAccessToken = loginService.requestNewAccessToken("Bearer $refreshToken").execute().body()?.data?.accessToken
                if (newAccessToken != null) {
                    // 새로운 액세스 토큰을 SharedPreferences에 저장
                    val editor = context.getSharedPreferences("tokens", Context.MODE_PRIVATE).edit()
                    editor.putString("access_token", newAccessToken)
                    editor.apply()

                    // 새로운 액세스 토큰을 포함한 새로운 요청 생성
                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()

                    // 새로운 요청 실행
                    return chain.proceed(newRequest)
                }
            }
        }
        // 만료된 토큰으로 요청하지 않았거나 새로운 액세스 토큰이 정상적으로 발급되지 않은 경우 기존 응답 반환
        return response
    }

    private fun getAccessTokenFromSharedPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("tokens", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }

    private fun getRefreshTokenFromSharedPreferences(): String? {
        val sharedPreferences = context.getSharedPreferences("tokens", Context.MODE_PRIVATE)
        return sharedPreferences.getString("refresh_token", null)
    }

    private fun requestNewAccessToken(refreshToken: String): String? {
        val loginService = RetrofitConnection.getInstanceWithoutToken(context).create(LoginService::class.java)
        val call = loginService.requestNewAccessToken("Bearer $refreshToken")
        val response = call.execute()
        return if (response.isSuccessful) {
            response.body()?.data?.accessToken
        } else {
            null
        }
    }
}
