package com.example.booksearchapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.booksearchapp.R
import com.example.booksearchapp.data.model.Book
import com.example.booksearchapp.databinding.FragmentBookBinding
import com.example.booksearchapp.ui.viewmodel.BookViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
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
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val book = args.book
        setWabView(book)
        saveBook(view, book)
        hideBottomNavigation()
        goBack(navController)
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

    // ConstructiveFirst, DestructiveLast 룰에 따라 super 의 위치를 지정
    override fun onPause() {
        binding.webView.onPause()
        super.onPause()
    }

    // ConstructiveFirst, DestructiveLast 룰에 따라 super 의 위치를 지정
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

    private fun goBack(navController: NavController) {
        binding.btnGoBack.setOnClickListener {
            navController.popBackStack()
        }
    }
}
