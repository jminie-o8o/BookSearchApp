package com.stark.booksearchapp.ui.bookreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.model.CEHModel
import com.stark.booksearchapp.data.repository.BookReportRepository
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
class BookReportRegisterViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    private val _error = MutableSharedFlow<CEHModel>()
    val error: SharedFlow<CEHModel> = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _error.emit(CoroutineException.handleThrowableWithCEHModel(throwable))
        }
    }

    fun saveBookReport(bookReport: BookReport) = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
        bookReportRepository.insertBookReport(bookReport)
    }

    // For Test
    val bookReportsForTest: Flow<List<BookReport>> = bookReportRepository.getBookReport()
}
