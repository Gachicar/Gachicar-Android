package com.example.gachicarapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gachicarapp.databinding.Dialog30mAlarmBinding

class CarDepartureDialog(
    private val userName: String,
    private val startTime: String,
    private val destination: String
) : DialogFragment() {

    private var _binding: Dialog30mAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = Dialog30mAlarmBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupDialog()

        return binding.root
    }

    private fun setupDialog() {
        val message = "$userName 님, $startTime 에 예약된 $destination 으로의 차량이 30분 후에 출발합니다."
        binding.tvCarDepartureMessage.text = message

        binding.btnCarDepartureOk.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
