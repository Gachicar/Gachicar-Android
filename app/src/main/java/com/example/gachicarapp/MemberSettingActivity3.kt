package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityStep03Binding
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.request.InviteMember
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.service.GroupService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MemberSettingActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityStep03Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityStep03Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val nicknameEditText1: EditText = findViewById(R.id.nicknameEditText1) // EditText ID 변경
        val nicknameEditText2: EditText = findViewById(R.id.nicknameEditText2) // EditText ID 변경
        val nicknameEditText3: EditText = findViewById(R.id.nicknameEditText3) // EditText ID 변경

        // invite1 버튼 클릭 리스너 설정
        binding.invite1.setOnClickListener {
            val nickname1 = nicknameEditText1.text.toString() // 변수명 변경
            inviteByNickname(nickname1)

        }

        // invite2 버튼 클릭 리스너 설정
        binding.invite2.setOnClickListener {
            val nickname2 = nicknameEditText2.text.toString() // 변수명 변경
            inviteByNickname(nickname2)
        }

        // invite3 버튼 클릭 리스너 설정
        binding.invite3.setOnClickListener {
            val nickname3 = nicknameEditText3.text.toString() // 변수명 변경
            inviteByNickname(nickname3)
        }

        binding.step3nextbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun inviteByNickname(nickname: String) {
        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)

        retrofitAPI.postInviteMember(InviteMember(nickname)).enqueue(object :
            Callback<ApiResponse<InviteMember>> {
            override fun onResponse(
                call: Call<ApiResponse<InviteMember>>,
                response: Response<ApiResponse<InviteMember>>
            ) {
                if (response.isSuccessful) {
                    showToastMessage()

                } else {
                    Toast.makeText(this@MemberSettingActivity3, "오류가 발생했습니다: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<InviteMember>>, t: Throwable) {
                Toast.makeText(this@MemberSettingActivity3, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()

            }
        })
    }
    private fun showToastMessage() {
        Toast.makeText(this, "초대가 완료되었습니다.\n상대방이 수락하면 그룹원으로 추가됩니다.", Toast.LENGTH_LONG).show()
    }


}
