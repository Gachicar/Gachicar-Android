package com.example.gachicarapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityStep01Binding
import com.example.gachicarapp.databinding.ActivityStep02Binding

class MemberSettingActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityStep02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityStep02Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // 여기서 바인딩을 사용하여 뷰에 액세스
        // 예: binding.textView.text = "Hello World!"
    }
}
