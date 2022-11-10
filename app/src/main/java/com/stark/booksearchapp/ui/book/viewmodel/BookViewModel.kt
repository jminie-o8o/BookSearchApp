package com.stark.booksearchapp.ui.book.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.booksearchapp.data.model.Book
import com.stark.booksearchapp.data.model.CEHModel
import com.stark.booksearchapp.data.repository.BookSearchRepository
import com.stark.booksearchapp.util.CoroutineException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookSearchRepository: BookSearchRepository,
) : ViewModel() {

    private val _error = MutableSharedFlow<CEHModel>()
    val error: SharedFlow<CEHModel> = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _error.emit(CoroutineException.checkThrowable(throwable))
        }
    }

    fun saveBook(book: Book) = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
        bookSearchRepository.insertBook(book)
    }

    // For Test
    val favoriteBooks: Flow<List<Book>> = bookSearchRepository.getFavoriteBooks()
}
