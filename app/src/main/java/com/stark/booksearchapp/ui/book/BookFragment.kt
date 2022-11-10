package com.stark.booksearchapp.ui.book

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.stark.booksearchapp.R
import com.stark.booksearchapp.data.model.Book
import com.stark.booksearchapp.databinding.FragmentBookBinding
import com.stark.booksearchapp.ui.MainActivity
import com.stark.booksearchapp.ui.book.viewmodel.BookViewModel
import com.stark.booksearchapp.util.collectStateFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment() {
    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<BookFragmentArgs>()
    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = com.stark.booksearchapp.databinding.FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val book = args.book
        setWabView(book)
        saveBook(view, book)
        hideBottomNavigation()
        goBack()
        registerBookReport(book)
        observeError()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWabView(book: Book) {
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(book.url)
        }
    }

    private fun saveBook(view: View, book: Book) {
        binding.fabFavorite.setOnClickListener {
            bookViewModel.saveBook(book)
            Snackbar.make(view, "책이 저장되었습니다.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun registerBookReport(book: Book) {
        binding.btnRegisterBookReport.setOnClickListener {
            val action = BookFragmentDirections.actionFragmentBookToRegisterBookReportFragment(book)
            findNavController().navigate(action)
        }
    }

    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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
        collectStateFlow(bookViewModel.error) { CEHModel ->
            if (CEHModel.throwable != null) Toast.makeText(
                requireContext(),
                CEHModel.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
