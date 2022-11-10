package com.stark.booksearchapp.ui.bookreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stark.booksearchapp.R
import com.stark.booksearchapp.databinding.FragmentBookReportBinding
import com.stark.booksearchapp.ui.adapter.BookReportPagingAdapter
import com.stark.booksearchapp.ui.MainActivity
import com.stark.booksearchapp.ui.bookreport.viewmodel.BookReportViewModel
import com.stark.booksearchapp.ui.search.adapter.BookSearchLoadStateAdapter
import com.stark.booksearchapp.ui.search.adapter.BookSearchPagingAdapter
import com.stark.booksearchapp.util.collectLatestStateFlow
import com.stark.booksearchapp.util.collectStateFlow
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

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
        bookReportAdapter = BookReportPagingAdapter()
        setupRecyclerView(bookReportAdapter)
        collectLatestStateFlow(bookReportViewModel.bookReports) {
            bookReportAdapter.submitData(it)
        }
        showBottomNavigation()
        handlePagingError(bookReportAdapter)
        observeError()
    }

    private fun setupRecyclerView(bookReportAdapter: BookReportPagingAdapter) {
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

    private fun handlePagingError(bookReportAdapter: BookReportPagingAdapter) {
        bookReportAdapter.addLoadStateListener { loadState ->
            val errorState = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            when (errorState?.error) {
                is HttpException -> Toast.makeText(
                    requireContext(),
                    "Http에러가 발생했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                is ConnectException -> Toast.makeText(
                    requireContext(),
                    "네트워크 연결이 불안정합니다.",
                    Toast.LENGTH_SHORT
                ).show()
                is SocketException -> Toast.makeText(
                    requireContext(),
                    "소켓 연결이 끊겼습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                is NullPointerException -> Toast.makeText(
                    requireContext(),
                    "NullPointer 오류입니다.",
                    Toast.LENGTH_SHORT
                ).show()
                is UnknownHostException ->  Toast.makeText(
                    requireContext(),
                    "도메인 주소를 찾지 못했습니다.\n네트워크 상태를 확인하세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeError() {
        collectStateFlow(bookReportViewModel.error) { CEHModel ->
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
