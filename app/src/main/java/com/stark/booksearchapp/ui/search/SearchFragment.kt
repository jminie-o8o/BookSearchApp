package com.stark.booksearchapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stark.booksearchapp.R
import com.stark.booksearchapp.databinding.FragmentSearchBinding
import com.stark.booksearchapp.ui.MainActivity
import com.stark.booksearchapp.ui.search.adapter.BookSearchLoadStateAdapter
import com.stark.booksearchapp.ui.search.adapter.BookSearchPagingAdapter
import com.stark.booksearchapp.ui.search.viewmodel.SearchViewModel
import com.stark.booksearchapp.util.collectLatestStateFlow
import com.stark.booksearchapp.util.collectStateFlow
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookSearchAdapter = BookSearchPagingAdapter()
        setupRecyclerView(bookSearchAdapter)
        setupLoadState()
        listenSearchWordChange()
        updateSearchWord()
        collectLatestStateFlow(searchViewModel.searchPagingResult) {
            bookSearchAdapter.submitData(it)
        }
        showBottomNavigation()
        handlePagingError(bookSearchAdapter)
        observeError()
    }

    private fun setupRecyclerView(bookSearchAdapter: BookSearchPagingAdapter) {
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = bookSearchAdapter.withLoadStateFooter(
                footer = BookSearchLoadStateAdapter(bookSearchAdapter::retry)
            )
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = SearchFragmentDirections.actionFragmentSearchToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    private fun listenSearchWordChange() {
        binding.etSearch.addTextChangedListener { text ->
            if (text != null) searchViewModel.handleSearchWord(text.toString())
        }
    }

    private fun updateSearchWord() {
        collectLatestStateFlow(searchViewModel.searchWord) { query ->
            searchViewModel.searchBooksPaging(query)
        }
    }

    private fun setupLoadState() {
        bookSearchAdapter.addLoadStateListener { combinedLoadStates ->
            val loadState = combinedLoadStates.source
            val isListEmpty = bookSearchAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            binding.tvEmptylist.isVisible = isListEmpty
            binding.rvSearchResult.isVisible = !isListEmpty

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
        }
    }

    private fun showBottomNavigation() {
        val bottomNavigation =
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigation.visibility = View.VISIBLE
    }

    private fun handlePagingError(bookSearchAdapter: BookSearchPagingAdapter) {
        bookSearchAdapter.addLoadStateListener { loadState ->
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
        collectStateFlow(searchViewModel.error) { CEHModel ->
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
