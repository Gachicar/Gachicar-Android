package com.example.gachicarapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityEditmemberBinding


class EditMActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditmemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityEditmemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.step3nextbtn.setOnClickListener {
            // 현재 액티비티 종료
            finish()
        }
    }
}
