package com.example.booksearchapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksearchapp.data.model.Book
import com.example.booksearchapp.data.model.SearchResponse
import com.example.booksearchapp.data.repository.BookSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookSearchViewModel(private val bookSearchRepository: BookSearchRepository) : ViewModel() {

    private val _searchResult = MutableLiveData<SearchResponse>()
    val searchResult: LiveData<SearchResponse> = _searchResult

    fun searchBooks(query: String) {
        viewModelScope.launch {
            val response = bookSearchRepository.searchBooks(query, getSortMode(), 1, 15)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    _searchResult.value = body
                }
            }
        }
    }

    // Room
    fun saveBook(book: Book) {
        viewModelScope.launch {
            bookSearchRepository.insertBook(book)
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookSearchRepository.deleteBook(book)
        }
    }

    val favoriteBooks: StateFlow<List<Book>> = bookSearchRepository.getFavoriteBooks()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    // DataStore
    fun saveSortMode(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            bookSearchRepository.saveSortMode(value)
        }
    }

    suspend fun getSortMode(): String {
        return withContext(Dispatchers.IO) {
            bookSearchRepository.getSortMode().first()
        }
    }
}
