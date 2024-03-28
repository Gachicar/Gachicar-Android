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
import com.example.gachicarapp.retrofit.request.CreateCar
import com.example.gachicarapp.retrofit.response.ApiResponse
import com.example.gachicarapp.retrofit.service.CarService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateCarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_step02)

        val retrofitAPI = RetrofitConnection.getInstance(this).create(CarService::class.java)

        val nicknameCarEditText: EditText = findViewById(R.id.nicknameCarEditText) // EditText ID 변경
        val numberCarEditText: EditText = findViewById(R.id.numberCarEditText) // EditText ID 변경

        val step2nextbtn: Button = findViewById(R.id.step2nextbtn) // 버튼 ID 변경

        step2nextbtn.isEnabled = false
        step2nextbtn.setBackgroundColor(ContextCompat.getColor(this, R.color.sky_blue))

        // Add text change listener to EditText
        nicknameCarEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Check if EditText has text
                if (s.isNullOrEmpty()) {
                    step2nextbtn.isEnabled = false
                    step2nextbtn.setBackgroundColor(ContextCompat.getColor(this@CreateCarActivity, R.color.sky_blue))
                } else {
                    step2nextbtn.isEnabled = true
                    step2nextbtn.setBackgroundColor(ContextCompat.getColor(this@CreateCarActivity, R.color.blue_light)) // Change to desired color
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 버튼 클릭 시 그룹 생성 후 차량 등록 화면으로 이동
        step2nextbtn.setOnClickListener {

            val carName = nicknameCarEditText.text.toString() // 변수명 변경
            val carNumber = numberCarEditText.text.toString() // 변수명 변경

            retrofitAPI.createCar(CreateCar(carName, carNumber)).enqueue(object :
                Callback<ApiResponse<CreateCar>> {
                override fun onResponse(
                    call: Call<ApiResponse<CreateCar>>,
                    response: Response<ApiResponse<CreateCar>>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CreateCarActivity,
                            "공유차량 생성 성공",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Start GroupActivity
                        val intent = Intent(this@CreateCarActivity, MemberSettingActivity3::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@CreateCarActivity, "오류가 발생했습니다: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<CreateCar>>, t: Throwable) {
                    Toast.makeText(this@CreateCarActivity, "네트워크 오류가 발생했습니다: ${t.message}", Toast.LENGTH_SHORT).show()

                }

            })
        }
    }
}
