package com.example.booksearchapp.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.booksearchapp.R
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.databinding.FragmentBookReportModifyBinding
import com.example.booksearchapp.ui.viewmodel.BookReportModifyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookReportModifyFragment : Fragment() {
    private var _binding: FragmentBookReportModifyBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<BookReportModifyFragmentArgs>()
    private val bookReportModifyViewModel: BookReportModifyViewModel by viewModels()
    private lateinit var pressBackCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pressBackCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 백 버튼을 눌렀을 때 로직을 여기서 구현
                val layoutInflater = LayoutInflater.from(requireContext())
                val view = layoutInflater.inflate(R.layout.cancel_modifying_fragment_dialog, null)
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setView(view)
                    .create()
                val buttonKeep = view.findViewById<TextView>(R.id.tv_btn_keep_write)
                val buttonCancel = view.findViewById<TextView>(R.id.tv_btn_cancel)
                buttonKeep.setOnClickListener {
                    alertDialog.dismiss()
                }
                buttonCancel.setOnClickListener {
                    findNavController().popBackStack()
                    alertDialog.dismiss()
                }
                alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, pressBackCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookReportModifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookReport = args.bookReport
        setDefaultView(bookReport)
        modifyBookReport(bookReport)
    }

    @SuppressLint("SetTextI18n")
    private fun setDefaultView(bookReport: BookReport) {
        val author = bookReport.author.toString().removeSurrounding("[", "]")
        val publisher = bookReport.publisher
        with(binding) {
            ivArticleImage.load(bookReport.thumbnail)
            tvTitle.text = bookReport.title
            tvAuthor.text = "$author | $publisher"
            etBookReportTitle.setText(bookReport.reportTitle)
            etBookReportContents.setText(bookReport.reportContents)
        }
    }

    private fun modifyBookReport(bookReport: BookReport) {
        bookReportModifyViewModel.saveBookReport(bookReport)
    }

    override fun onDetach() {
        super.onDetach()
        pressBackCallback.remove()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
