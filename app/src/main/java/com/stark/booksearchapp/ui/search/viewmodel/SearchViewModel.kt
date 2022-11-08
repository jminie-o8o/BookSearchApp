package com.stark.booksearchapp.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stark.booksearchapp.data.model.Book
import com.stark.booksearchapp.data.model.CEHModel
import com.stark.booksearchapp.data.repository.BookSearchRepository
import com.stark.booksearchapp.util.CoroutineException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookSearchRepository: BookSearchRepository,
) : ViewModel() {

    private val _searchWord = MutableSharedFlow<String>()
    val searchWord = _searchWord.debounce { 200 }

    private val _searchPagingResult = MutableStateFlow<PagingData<Book>>(PagingData.empty())
    val searchPagingResult: StateFlow<PagingData<Book>> = _searchPagingResult.asStateFlow()

    private val _error = MutableSharedFlow<CEHModel>()
    val error: SharedFlow<CEHModel> = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _error.emit(CoroutineException.checkThrowable(throwable))
        }
    }

    fun handleSearchWord(word: String) {
        viewModelScope.launch(exceptionHandler) {
            _searchWord.emit(word)
        }
    }

    fun searchBooksPaging(query: String) {
        viewModelScope.launch(exceptionHandler) {
            bookSearchRepository.searchBooksPaging(query, getSortMode())
                .cachedIn(viewModelScope)
                .collect {
                    _searchPagingResult.value = it
                }
        }
    }

    companion object {
        private const val SAVE_STATE_KEY = "query"
    }

    // DataStore
    suspend fun getSortMode() = withContext(Dispatchers.IO) {
        bookSearchRepository.getSortMode().first()
    }
}
