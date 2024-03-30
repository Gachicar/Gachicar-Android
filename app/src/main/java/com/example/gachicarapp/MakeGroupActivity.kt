package com.example.gachicarapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityNogroupBinding

class MakeGroupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNogroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityNogroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLater.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
