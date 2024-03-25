package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityStep01Binding
import com.example.gachicarapp.databinding.ActivityStep03Binding

class MemberSettingActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityStep03Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityStep03Binding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
