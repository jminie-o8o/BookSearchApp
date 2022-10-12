package com.example.booksearchapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.booksearchapp.data.model.Book
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.databinding.FragmentRegisterBookReportBinding
import com.example.booksearchapp.ui.viewmodel.BookReportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterBookReportFragment : Fragment() {
    private var _binding: FragmentRegisterBookReportBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RegisterBookReportFragmentArgs>()
    private val bookReportViewModel: BookReportViewModel by viewModels()

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
        binding.ivArticleImage.load(book.thumbnail)
        binding.tvTitle.text = book.title
        binding.tvAuthor.text = "$author | $publisher"
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
                reportContents = binding.tlBookReportContents.editText?.text?.toString() ?: ""
            )
            bookReportViewModel.saveBookReport(bookReport)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
