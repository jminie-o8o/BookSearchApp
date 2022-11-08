package com.stark.booksearchapp.ui.view.bookreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.repository.BookReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReportDetailViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    private val _bookReport = MutableStateFlow<BookReport?>(null)
    val bookReport: StateFlow<BookReport?> get() = _bookReport

    fun getBookReportDetail(isbn: String) = viewModelScope.launch(Dispatchers.IO) {
        _bookReport.value = bookReportRepository.getBookReportDetail(isbn)
    }

    fun deleteBookReport(bookReport: BookReport) = viewModelScope.launch(Dispatchers.IO) {
        bookReportRepository.deleteBookReport(bookReport)
    }

    // For Test
    val bookReportDetailList = mutableListOf<BookReport>()

    fun getBookReportDetailTest() = viewModelScope.launch(Dispatchers.IO) {
        bookReportDetailList.add(bookReportRepository.getBookReportDetail("a"))
    }
}
