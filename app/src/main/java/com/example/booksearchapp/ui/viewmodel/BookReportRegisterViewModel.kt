package com.example.booksearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.data.repository.BookReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReportRegisterViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    // Room
    fun saveBookReport(bookReport: BookReport) = viewModelScope.launch(Dispatchers.IO) {
        bookReportRepository.insertBookReport(bookReport)
    }

    // For Test
    val bookReportsForTest: Flow<List<BookReport>> = bookReportRepository.getBookReport()
}
