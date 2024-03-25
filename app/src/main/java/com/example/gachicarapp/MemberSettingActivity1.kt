package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityStep01Binding

class MemberSettingActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityStep01Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityStep01Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.step1nextbtn.setOnClickListener {
            val intent = Intent(this, MemberSettingActivity2::class.java)
            startActivity(intent)
        }

    }
}
