package com.example.booksearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.data.repository.BookReportRepository
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

    fun getBookReportDetail(isbn: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _bookReport.value = bookReportRepository.getBookReportDetail(isbn)
        }
    }
}
