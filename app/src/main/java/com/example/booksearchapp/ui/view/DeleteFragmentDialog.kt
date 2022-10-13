package com.example.booksearchapp.ui.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.databinding.DeleteFragmentDialogBinding
import com.example.booksearchapp.ui.viewmodel.BookReportDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFragmentDialog : DialogFragment() {

    private var _binding: DeleteFragmentDialogBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DeleteFragmentDialogArgs>()
    private val bookReportDetailViewModel: BookReportDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DeleteFragmentDialogBinding.inflate(inflater, container, false)
        dialog?.window
            ?.setBackgroundDrawable(ColorDrawable(Color.green(10))) // 다이얼로그의 곡선 주변에 배경색을 맞춰주는 코드
        dialog?.window
            ?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false) // 다이얼로그 외부의 영역 터치 시 취소 불가능
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookReport = args.bookReport
        keepBookReport()
        deleteWrite(bookReport)
    }

    private fun keepBookReport() {
        binding.tvBtnKeep.setOnClickListener { dismiss() }
    }

    private fun deleteWrite(bookReport: BookReport) {
        binding.tvBtnDelete.setOnClickListener {
            bookReportDetailViewModel.deleteBookReport(bookReport)
            val action = DeleteFragmentDialogDirections.actionDeleteFragmentDialogToFragmentBookReport()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
