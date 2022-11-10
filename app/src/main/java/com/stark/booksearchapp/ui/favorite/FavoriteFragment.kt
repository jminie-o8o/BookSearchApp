package com.stark.booksearchapp.ui.favorite

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.stark.booksearchapp.R
import com.stark.booksearchapp.databinding.FragmentFavoriteBinding
import com.stark.booksearchapp.ui.MainActivity
import com.stark.booksearchapp.ui.favorite.viewmodel.FavoriteViewModel
import com.stark.booksearchapp.ui.search.adapter.BookSearchPagingAdapter
import com.stark.booksearchapp.util.collectLatestStateFlow
import com.stark.booksearchapp.util.collectStateFlow
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var bookSearchAdapter: BookSearchPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookSearchAdapter = BookSearchPagingAdapter()
        setupRecyclerView(bookSearchAdapter)
        setupTouchHelper(view)
        collectLatestStateFlow(favoriteViewModel.favoritePagingBooks) {
            bookSearchAdapter.submitData(it)
        }
        showBottomNavigation()
        handlePagingError(bookSearchAdapter)
        observeError()
    }

    private fun setupRecyclerView(bookSearchAdapter: BookSearchPagingAdapter) {
        binding.rvFavoriteBooks.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = bookSearchAdapter
        }
        bookSearchAdapter.setOnItemClickListener {
            val action = FavoriteFragmentDirections.actionFragmentFavoriteToFragmentBook(it)
            findNavController().navigate(action)
        }
    }

    private fun setupTouchHelper(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val book = bookSearchAdapter.peek(position)
                book?.let { book ->
                    favoriteViewModel.deleteBook(book)
                    Snackbar.make(view, "즐겨찾기가 취소되었어요.", Snackbar.LENGTH_SHORT).apply {
                        setAction("취소") {
                            favoriteViewModel.saveBook(book)
                        }
                    }.show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavoriteBooks)
        }
    }

    private fun showBottomNavigation() {
        val bottomNavigation = (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
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
        collectStateFlow(favoriteViewModel.error) { CEHModel ->
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
