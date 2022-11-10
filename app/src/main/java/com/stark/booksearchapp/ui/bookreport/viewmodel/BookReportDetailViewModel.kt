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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReportDetailViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    private val _bookReport = MutableStateFlow<BookReport?>(null)
    val bookReport: StateFlow<BookReport?> get() = _bookReport

    private val _error = MutableSharedFlow<CEHModel>()
    val error: SharedFlow<CEHModel> = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _error.emit(CoroutineException.checkThrowableAtViewModel(throwable))
        }
    }

    fun getBookReportDetail(isbn: String) = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
        _bookReport.value = bookReportRepository.getBookReportDetail(isbn)
    }

    fun deleteBookReport(bookReport: BookReport) = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
        bookReportRepository.deleteBookReport(bookReport)
    }

    // For Test
    val bookReportDetailList = mutableListOf<BookReport>()

    fun getBookReportDetailTest() = viewModelScope.launch(Dispatchers.IO) {
        bookReportDetailList.add(bookReportRepository.getBookReportDetail("a"))
    }
}
