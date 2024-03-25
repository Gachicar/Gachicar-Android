package com.example.gachicarapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityEditgroupinfoBinding


class EditGActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditgroupinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityEditgroupinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextbtn.setOnClickListener {
            // 현재 액티비티 종료
            finish()
        }

    }
}
