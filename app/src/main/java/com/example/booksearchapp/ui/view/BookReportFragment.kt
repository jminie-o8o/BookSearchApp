package com.example.booksearchapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booksearchapp.R
import com.example.booksearchapp.databinding.FragmentBookReportBinding
import com.example.booksearchapp.ui.adapter.BookReportPagingAdapter
import com.example.booksearchapp.ui.adapter.BookSearchLoadStateAdapter
import com.example.booksearchapp.ui.viewmodel.BookReportRegisterViewModel
import com.example.booksearchapp.ui.viewmodel.BookReportViewModel
import com.example.booksearchapp.util.collectLatestStateFlow
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookReportFragment : Fragment() {
    private var _binding: FragmentBookReportBinding? = null
    private val binding get() = _binding!!

    private val bookReportViewModel: BookReportViewModel by viewModels()
    private lateinit var bookReportAdapter: BookReportPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectLatestStateFlow(bookReportViewModel.bookReports) {
            bookReportAdapter.submitData(it)
        }
        showBottomNavigation()
    }

    private fun setupRecyclerView() {
        bookReportAdapter = BookReportPagingAdapter()
        binding.rvBookReport.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = bookReportAdapter.withLoadStateFooter(
                footer = BookSearchLoadStateAdapter(bookReportAdapter::retry)
            )
            bookReportAdapter.setOnItemClickListener {
                val action = BookReportFragmentDirections.actionFragmentBookReportToBookReportDetailFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun showBottomNavigation() {
        val bottomNavigation = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigation.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
