package com.stark.booksearchapp.ui.view.bookreport

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
import com.stark.booksearchapp.R
import com.stark.booksearchapp.data.model.Book
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.databinding.FragmentRegisterBookReportBinding
import com.stark.booksearchapp.ui.view.bookreport.viewmodel.BookReportRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class BookReportRegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBookReportBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<BookReportRegisterFragmentArgs>()
    private val bookReportRegisterViewModel: BookReportRegisterViewModel by viewModels()
    private lateinit var pressBackCallback: OnBackPressedCallback


    override fun onAttach(context: Context) {
        super.onAttach(context)
        pressBackCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val layoutInflater = LayoutInflater.from(requireContext())
                val view = layoutInflater.inflate(R.layout.cancel_fragment_dialog, null)
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
        _binding = FragmentRegisterBookReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val book = args.book
        setBookInformation(book)
        saveBookReport(book)
    }

    @SuppressLint("SetTextI18n")
    private fun setBookInformation(book: Book) {
        val author = book.authors.toString().removeSurrounding("[", "]")
        val publisher = book.publisher
        with(binding) {
            ivArticleImage.load(book.thumbnail)
            tvTitle.text = book.title
            tvAuthor.text = "$author | $publisher"
        }
    }

    private fun saveBookReport(book: Book) {
        binding.btnRegisterBookReport.setOnClickListener {
            val author = book.authors.toString().removeSurrounding("[", "]")
            val bookReport = BookReport(
                isbn = book.isbn,
                thumbnail = book.thumbnail,
                title = book.title,
                author = author,
                publisher = book.publisher,
                reportTitle = binding.tlBookReportTitle.editText?.text?.toString() ?: "",
                reportContents = binding.tlBookReportContents.editText?.text?.toString() ?: "",
                date = getDate()
            )
            bookReportRegisterViewModel.saveBookReport(bookReport)
            val action = BookReportRegisterFragmentDirections.actionRegisterBookReportFragmentToBookReportDetailFragment(bookReport)
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        return simpleDateFormat.format(date)
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
