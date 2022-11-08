package com.stark.booksearchapp.ui.view.bookreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stark.booksearchapp.R
import com.stark.booksearchapp.databinding.FragmentBookReportBinding
import com.stark.booksearchapp.ui.adapter.BookReportPagingAdapter
import com.stark.booksearchapp.ui.view.MainActivity
import com.stark.booksearchapp.ui.view.bookreport.viewmodel.BookReportViewModel
import com.stark.booksearchapp.ui.view.search.adapter.BookSearchLoadStateAdapter
import com.stark.booksearchapp.util.collectLatestStateFlow
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
