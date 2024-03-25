package com.example.gachicarapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gachicarapp.databinding.ActivityEditcarinfoBinding


class EditCarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditcarinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)
        binding = ActivityEditcarinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.step2nextbtn.setOnClickListener {

            finish()
        }


    }
}
