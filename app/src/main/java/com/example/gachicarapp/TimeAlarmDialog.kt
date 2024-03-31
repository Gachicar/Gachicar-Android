package com.example.gachicarapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gachicarapp.databinding.DialogTimeAlarmBinding

class TimeAlarmDialog(
    private val userName: String,
    private val startTime: String,
    private val destination: String
) : DialogFragment() {

    private var _binding: DialogTimeAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogTimeAlarmBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        setupDialog()

        return binding.root
    }

    private fun setupDialog() {
        binding.tvTimeAlarmMessage.text = getString(R.string.time_alarm_message, userName, startTime, destination)
        binding.btnTimeAlarmOk.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
