package com.stark.booksearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.repository.BookReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReportModifyViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    // Room
    fun saveBookReport(bookReport: BookReport) = viewModelScope.launch(Dispatchers.IO) {
        bookReportRepository.insertBookReport(bookReport)
    }

    // For Test
    val bookReportsForTest: Flow<List<BookReport>> = bookReportRepository.getBookReport()
}
