package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.response.UserNickname
import com.example.gachicarapp.retrofit.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNickActivity : AppCompatActivity() {
    private lateinit var viewModel: SharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        val nickEditText: EditText = findViewById(R.id.editTextNick) // EditText ID 추가
        val submitNickButton: Button = findViewById(R.id.submitNickButton)
        val retrofitAPI = RetrofitConnection.getInstance(this).create(LoginService::class.java)

        // Initially disable button and change its color
        submitNickButton.isEnabled = false
        submitNickButton.setBackgroundColor(ContextCompat.getColor(this, R.color.sky_blue))

        // Add text change listener to EditText
        nickEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Check if EditText has text
                if (s.isNullOrEmpty()) {
                    submitNickButton.isEnabled = false
                    submitNickButton.setBackgroundColor(ContextCompat.getColor(this@EditNickActivity, R.color.sky_blue))
                } else {
                    submitNickButton.isEnabled = true
                    submitNickButton.setBackgroundColor(ContextCompat.getColor(this@EditNickActivity, R.color.blue_light)) // Change to desired color
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        submitNickButton.setOnClickListener {
            val nick = nickEditText.text.toString()
            retrofitAPI.patchNick(UserNickname(nick)).enqueue(object : Callback<ApiResponse<UserNickname>> {
                override fun onResponse(call: Call<ApiResponse<UserNickname>>, response: Response<ApiResponse<UserNickname>>) {
                    if (response.isSuccessful) {

                        val responseBody = response.body()
                        if (responseBody != null) {
                            if (responseBody.code.equals(201)) {

                                Toast.makeText(
                                    this@EditNickActivity,
                                    "닉네임이 업데이트되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Start GroupActivity
                                val intent = Intent(this@EditNickActivity, GroupActivity::class.java)
                                startActivity(intent)

                            } else if (responseBody.data.equals("중복된 닉네임입니다.")) {
                                Toast.makeText(
                                    this@EditNickActivity,
                                    "중복된 닉네임입니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@EditNickActivity, "오류가 발생했습니다: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<UserNickname>>, t: Throwable) {
                    Toast.makeText(this@EditNickActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
