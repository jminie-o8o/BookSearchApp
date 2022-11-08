package com.stark.booksearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.repository.BookReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookReportViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    val bookReports: StateFlow<PagingData<BookReport>> =
        bookReportRepository.getBookReportPaging()
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    fun deleteBookReport(bookReport: BookReport) = viewModelScope.launch(Dispatchers.IO) {
        bookReportRepository.deleteBookReport(bookReport)
    }
}
