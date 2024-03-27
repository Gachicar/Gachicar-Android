package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.gachicarapp.retrofit.RetrofitConnection
import com.example.gachicarapp.retrofit.request.CreateGroup
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.service.GroupService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_step01)

        val retrofitAPI = RetrofitConnection.getInstance(this).create(GroupService::class.java)

        val groupNameEditText: EditText = findViewById(R.id.groupNameEditText) // EditText ID 추가
        val groupDescEditText: EditText = findViewById(R.id.groupDescEditText) // EditText ID 추가

        val step1nextbtn: Button = findViewById(R.id.step1nextbtn)

        step1nextbtn.isEnabled = false
        step1nextbtn.setBackgroundColor(ContextCompat.getColor(this, R.color.sky_blue))

        // Add text change listener to EditText
        groupNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Check if EditText has text
                if (s.isNullOrEmpty()) {
                    step1nextbtn.isEnabled = false
                    step1nextbtn.setBackgroundColor(ContextCompat.getColor(this@CreateGroupActivity, R.color.sky_blue))
                } else {
                    step1nextbtn.isEnabled = true
                    step1nextbtn.setBackgroundColor(ContextCompat.getColor(this@CreateGroupActivity, R.color.blue_light)) // Change to desired color
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 버튼 클릭 시 그룹 생성 후 차량 등록 화면으로 이동
        step1nextbtn.setOnClickListener {

            val groupName = groupNameEditText.text.toString()
            val groupDesc = groupDescEditText.text.toString()

            retrofitAPI.postGroupNameData(CreateGroup(groupName=groupName, groupDesc=groupDesc)).enqueue(object :
                Callback<ApiResponse<CreateGroup>> {
                override fun onResponse(
                    call: Call<ApiResponse<CreateGroup>>,
                    response: Response<ApiResponse<CreateGroup>>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CreateGroupActivity,
                            "그룹 생성 성공",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Start GroupActivity
                        val intent = Intent(this@CreateGroupActivity, CreateCarActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@CreateGroupActivity, "오류가 발생했습니다: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<CreateGroup>>, t: Throwable) {
                    Toast.makeText(this@CreateGroupActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()

                }

            })
        }
    }
}