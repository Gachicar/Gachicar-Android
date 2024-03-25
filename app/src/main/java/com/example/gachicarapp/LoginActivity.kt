package com.example.gachicarapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gachicarapp.KaKaoAuthViewModel.Companion.TAG
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.SocialSignUpandLogin
import com.example.gachicarapp.retrofit.service.LoginService
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val kaKaoAuthViewModel: KaKaoAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: ImageButton = findViewById(R.id.loginButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val statusTextView: TextView = findViewById(R.id.statusTextView)

        // Observe the login state and update UI accordingly
        lifecycleScope.launchWhenStarted {
            kaKaoAuthViewModel.isLoggedIn.collect { isLoggedIn ->
                statusTextView.text = if (isLoggedIn) "로그인 상태" else "로그아웃 상태"
            }
        }

        loginButton.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    // Handle error
                } else if (token != null) {
                    Log.e(TAG, "로그인 성공 ${token.accessToken}")
                    kaKaoAuthViewModel.updateLoginState(true)
                    sendTokenToServer(token.accessToken)
                }
            }
        }

        logoutButton.setOnClickListener { kaKaoAuthViewModel.kakoLogout() }
    }

    private fun sendTokenToServer(accessToken: String) {
        // Retrofit을 통해 서버와 통신하기 위한 서비스 생성
        val service = RetrofitConnection.getInstance(this).create(LoginService::class.java)

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
                    }

                    Log.d("LoginActivity", "로그인 성공: 이동 준비됨");

                    // 메인 액티비티로 이동
                    navigateToMainActivity()
                } else {
                    // 서버 응답이 실패한 경우
                    Log.e("LoginActivity", "서버 응답 에러: " + (response.errorBody()?.string() ?: "Unknown Error"))
                }
            }

            override fun onFailure(call: Call<SocialSignUpandLogin>, t: Throwable) {
                // 통신 실패 시 처리
                Log.e("LoginActivity", "통신 실패: " + t.message)
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

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish LoginActivity so user can't return to it with the back button
    }
}