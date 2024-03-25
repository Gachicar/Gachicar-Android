package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityStep02Binding

class MemberSettingActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityStep02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityStep02Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.step2nextbtn.setOnClickListener {
            val intent = Intent(this, MemberSettingActivity3::class.java)
            startActivity(intent)
        }

    }
}
