package com.example.booksearchapp.ui.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.booksearchapp.databinding.CancelFragmentDialogBinding

class CancelDialogFragment : DialogFragment() {

    private var _binding: CancelFragmentDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CancelFragmentDialogBinding.inflate(inflater, container, false)
        dialog?.window
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 다이얼로그의 곡선 주변에 배경색을 맞춰주는 코드
        dialog?.window
            ?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false) // 다이얼로그 외부의 영역 터치 시 취소 불가능
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keepWrite()
        cancelWrite()
    }

    private fun keepWrite() {
        binding.tvBtnKeepWrite.setOnClickListener { dismiss() }
    }

    private fun cancelWrite() {
        binding.tvBtnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
