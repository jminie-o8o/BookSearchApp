package com.example.booksearchapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.booksearchapp.R
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.databinding.FragmentBookReportDetailBinding
import com.example.booksearchapp.ui.viewmodel.BookReportDetailViewModel
import com.example.booksearchapp.util.collectStateFlow
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookReportDetailFragment : Fragment() {
    private var _binding: FragmentBookReportDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<BookReportDetailFragmentArgs>()
    private val bookReportDetailViewModel: BookReportDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookReport = args.bookReport
        getBookReport(bookReport)
        setBookReport()
        hideBottomNavigation()
        goBack()
    }

    private fun getBookReport(bookReport: BookReport) {
        bookReportDetailViewModel.getBookReportDetail(bookReport.isbn)
    }

    @SuppressLint("SetTextI18n")
    private fun setBookReport() {
        collectStateFlow(bookReportDetailViewModel.bookReport) { bookReport ->
            if (bookReport != null) {
                val author = bookReport.author.toString().removeSurrounding("[", "]")
                val publisher = bookReport.publisher
                binding.ivArticleImage.load(bookReport.thumbnail)
                binding.tvTitle.text = bookReport.title
                binding.tvAuthor.text = "$author | $publisher"
                binding.tvBookReportTitleDetail.text = bookReport.reportTitle
                binding.tvBookReportContentsDetail.text = bookReport.reportContents
            }
        }
    }

    private fun hideBottomNavigation() {
        val bottomNavigation = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigation.visibility = View.GONE
    }

    private fun goBack() {
        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
