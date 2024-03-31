package com.example.gachicarapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gachicarapp.databinding.DialogConfirmBinding

interface ConfirmDialogInterface {
    fun onClickYesButton(id: Int)
    fun onClickNoButton(id: Int)
}

class ConfirmDialog(
    private val confirmDialogInterface: ConfirmDialogInterface,
    private val title: String,
    private val content: String?,
    private val buttonText: String,
    private val id: Int
) : DialogFragment() {

    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.dialogTitleTv.text = title

        if (content == null) {
            binding.dialogDescTv.visibility = View.GONE
        } else {
            binding.dialogDescTv.text = content
        }

        binding.dialogYesBtn.text = buttonText

        if (id == -1) {
            binding.dialogNoBtn.visibility = View.GONE
        }

        binding.dialogNoBtn.setOnClickListener {
            dismiss()
        }

        binding.dialogYesBtn.setOnClickListener {
            confirmDialogInterface.onClickYesButton(id)
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
