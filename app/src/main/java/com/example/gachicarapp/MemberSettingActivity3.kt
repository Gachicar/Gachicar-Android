package com.example.gachicarapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityStep01Binding
import com.example.gachicarapp.databinding.ActivityStep03Binding

class MemberSettingActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityStep03Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityStep03Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // invite1 버튼 클릭 리스너 설정
        binding.invite1.setOnClickListener {
            showToastMessage()
        }

        // invite2 버튼 클릭 리스너 설정
        binding.invite2.setOnClickListener {
            showToastMessage()
        }

        // invite3 버튼 클릭 리스너 설정
        binding.invite3.setOnClickListener {
            showToastMessage()
        }
        binding.step3nextbtn.setOnClickListener {

            finish()
        }

    }
    private fun showToastMessage() {
        Toast.makeText(this, "초대가 완료되었습니다.\n상대방이 수락하면 그룹원으로 추가됩니다.", Toast.LENGTH_LONG).show()
    }


}
