package com.stark.booksearchapp.ui.bookreport

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stark.booksearchapp.R
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.databinding.FragmentBookReportDetailBinding
import com.stark.booksearchapp.ui.MainActivity
import com.stark.booksearchapp.ui.bookreport.viewmodel.BookReportDetailViewModel
import com.stark.booksearchapp.util.collectStateFlow
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
        setToolBarMenuListener(bookReport)
        observeError()
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

    private fun setToolBarMenuListener(bookReport: BookReport) {
        binding.toolBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.modify_book_report -> {
                    findNavController().navigate(BookReportDetailFragmentDirections.actionFragmentBookReportDetailToFragmentBookReportModify(bookReport))
                    true
                }
                R.id.delete_book_report -> {
                    showAlertDialog(bookReport)
                    true
                }
                else -> false
            }
        }
    }

    private fun showAlertDialog(bookReport: BookReport) {
        val layoutInflater = LayoutInflater.from(requireContext())
        val view = layoutInflater.inflate(R.layout.delete_fragment_dialog, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        val buttonKeep = view.findViewById<TextView>(R.id.tv_btn_keep)
        val buttonDelete = view.findViewById<TextView>(R.id.tv_btn_delete)
        buttonKeep.setOnClickListener {
            alertDialog.dismiss()
        }
        buttonDelete.setOnClickListener {
            bookReportDetailViewModel.deleteBookReport(bookReport)
            findNavController().popBackStack()
            alertDialog.dismiss()
        }
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
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

    private fun observeError() {
        collectStateFlow(bookReportDetailViewModel.error) { CEHModel ->
            if (CEHModel.throwable != null) Toast.makeText(
                requireContext(),
                CEHModel.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
