package com.example.gachicarapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.SocialSignUpandLogin
import com.example.gachicarapp.retrofit.service.LoginService
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: ImageButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    // Handle error
                } else if (token != null) {
                    Timber.tag(TAG).e("로그인 성공 %s", token.accessToken)
                    sendTokenToServer(token.accessToken)
                }
            }
        }

    }

    private fun sendTokenToServer(accessToken: String) {
        // Retrofit을 통해 서버와 통신하기 위한 서비스 생성
        val service = RetrofitConnection.getInstanceWithoutToken(this).create(LoginService::class.java)

        // 서버로 전송할 액세스 토큰을 헤더에 추가하여 요청을 생성
        val call = service.token("Bearer $accessToken")

        // 서버로 요청을 비동기적으로 실행
        call.enqueue(object : Callback<SocialSignUpandLogin> {
            override fun onResponse(call: Call<SocialSignUpandLogin>, response: Response<SocialSignUpandLogin>) {
                if (response.isSuccessful) {
                    // 서버 응답이 성공적으로 도착한 경우
                    val socialSignUpandLogin = response.body()

                    // 응답으로 받은 새로운 토큰들을 SharedPreferences에 저장
                    socialSignUpandLogin?.let { tokenResponse ->
                        saveTokens(tokenResponse.data.accessToken, tokenResponse.data.refreshToken)
                        navigateToNickOrMain(tokenResponse.data.type)
                    }

                } else {
                    // 서버 응답이 실패한 경우
                    Log.e("LoginActivity", "서버 응답 에러: " + (response.errorBody()?.string() ?: "Unknown Error"))
                }
            }

            override fun onFailure(call: Call<SocialSignUpandLogin>, t: Throwable) {
                // 통신 실패 시 처리
                Timber.tag("LoginActivity").e("통신 실패: %s", t.message)
            }
        })
    }

    // 새로운 액세스 토큰 및 리프레시 토큰을 SharedPreferences에 저장하는 함수
    private fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("tokens", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.apply()
    }

    // 회원가입 시에는 닉네임 설정 화면으로 이동,
    // 로그인 시에는 메인 화면으로 이동
    private fun navigateToNickOrMain(type: String) {
        if (type.equals("Signup")) {
            val intent = Intent(this, EditNickActivity::class.java)
            startActivity(intent)
        } else if (type.equals("Login")) {
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish LoginActivity so user can't return to it with the back button
    }
}