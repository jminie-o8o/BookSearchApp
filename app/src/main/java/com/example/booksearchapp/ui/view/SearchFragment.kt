package com.example.booksearchapp.ui.view

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booksearchapp.databinding.FragmentSearchBinding
import com.example.booksearchapp.ui.adapter.BookSearchLoadStateAdapter
import com.example.booksearchapp.ui.adapter.BookSearchPagingAdapter
import com.example.booksearchapp.ui.viewmodel.BookSearchViewModel
import com.example.booksearchapp.util.Constants.SEARCH_BOOKS_TIME_DELAY
import com.example.booksearchapp.util.collectLatestStateFlow

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookSearchViewModel: BookSearchViewModel
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
        setupRecyclerView()
        searchBooks()
        setupLoadState()
        bookSearchViewModel = (activity as MainActivity).bookSearchViewModel
        collectLatestStateFlow(bookSearchViewModel.searchPagingResult) {
            bookSearchAdapter.submitData(it)
        }
    }

    private fun setupRecyclerView() {
        bookSearchAdapter = BookSearchPagingAdapter()
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = bookSearchAdapter.withLoadStateFooter(
                footer = BookSearchLoadStateAdapter(bookSearchAdapter::retry)
            )
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = SearchFragmentDirections.actionFragmentSearchToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    private fun searchBooks() {
        var startTime = System.currentTimeMillis()
        var endTime: Long

        binding.etSearch.addTextChangedListener { text: Editable? ->
            endTime = System.currentTimeMillis()
            if (endTime - startTime >= SEARCH_BOOKS_TIME_DELAY) {
                text?.let {
                    val query = it.toString().trim()
                    if (query.isNotEmpty()) {
                        bookSearchViewModel.searchBookPaging(query)
                    }
                }
            }
            startTime = endTime
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupLoadState() {
        bookSearchAdapter.addLoadStateListener { combinedLoadStates ->  // PagingSource 의 로딩상태를 가지고 있음
            val loadState = combinedLoadStates.source
            // 리스트가 비어있는지 판정
            val isListEmpty = bookSearchAdapter.itemCount < 1
                    && loadState.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached

            binding.tvEmptylist.isVisible = isListEmpty
            binding.rvSearchResult.isVisible = !isListEmpty

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
        }
    }
}
