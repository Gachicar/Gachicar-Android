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
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.socialSignUpandLogin
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
                    kaKaoAuthViewModel.updateLoginState(true)
                    sendTokenToServer(accessToken = token.accessToken, refreshToken = token.refreshToken)
                }
            }
        }

        logoutButton.setOnClickListener { kaKaoAuthViewModel.kakoLogout() }
    }

    private fun sendTokenToServer(accessToken: String, refreshToken: String) {
        // 액세스 토큰과 리프레시 토큰을 SharedPreferences에 저장
        val sharedPreferences = getSharedPreferences("tokens", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.putString("refresh_token", refreshToken)
        editor.apply()

        // 서버로 액세스 토큰 전송
        val service = RetrofitConnection.getInstance(this).create(LoginService::class.java)
//        val service = RetrofitConnection.getInstance().create(LoginService::class.java)
        val call = service.token("Bearer $accessToken")

        call.enqueue(object : Callback<socialSignUpandLogin> {
            override fun onResponse(call: Call<socialSignUpandLogin>, response: Response<socialSignUpandLogin>) {
                if (response.isSuccessful) {
                    Log.d("LoginActivity", "로그인 성공: 이동 준비됨");
                    navigateToMainActivity();
                } else {
                    Log.e("LoginActivity", "서버 응답 에러: " + (response.errorBody()?.string() ?: "Unknown Error"))

                }

            }

            override fun onFailure(call: Call<socialSignUpandLogin>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish LoginActivity so user can't return to it with the back button
    }
}