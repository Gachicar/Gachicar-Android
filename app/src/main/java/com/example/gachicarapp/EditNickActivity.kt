package com.example.gachicarapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.response.updateUserNickname
import com.example.gachicarapp.retrofit.service.AppServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditNickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        val nickEditText: EditText = findViewById(R.id.editTextNick) // EditText ID 추가
        val submitNickButton: Button = findViewById(R.id.submitNickButton)
        val retrofitAPI = RetrofitConnection.getInstance(this).create(AppServices::class.java)
        submitNickButton.setOnClickListener {
            val nick = nickEditText.text.toString()
            retrofitAPI.patchNick(updateUserNickname(nick)).enqueue(object : Callback<updateUserNickname> {
                override fun onResponse(call: Call<updateUserNickname>, response: Response<updateUserNickname>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditNickActivity, "닉네임이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EditNickActivity, "오류가 발생했습니다: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<updateUserNickname>, t: Throwable) {
                    Toast.makeText(this@EditNickActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
