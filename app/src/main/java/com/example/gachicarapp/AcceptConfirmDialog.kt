package com.example.gachicarapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gachicarapp.databinding.DialogAcceptConfirmBinding

interface AcceptConfirmDialogInterface {
    fun onAcceptanceConfirmed()
}

class AcceptConfirmDialog(
    private val dialogInterface: AcceptConfirmDialogInterface,
    private val sender: String,
    private val groupId: Int,
    private val createdAt: String
) : DialogFragment() {

    private lateinit var binding: DialogAcceptConfirmBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAcceptConfirmBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupDialog()

        return binding.root
    }

    private fun setupDialog() {
        val content = "$sender 님이 그룹 초대를 수락했습니다. 생성된 그룹 ID는 $groupId 입니다. 초대 시간: $createdAt"

        binding.tvAcceptConfirmMessage.text = content

        binding.btnAcceptConfirmOk.setOnClickListener {
            dialogInterface.onAcceptanceConfirmed()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
