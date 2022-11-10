package com.stark.booksearchapp.ui.bookreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.model.CEHModel
import com.stark.booksearchapp.data.repository.BookReportRepository
import com.stark.booksearchapp.util.CoroutineException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class BookReportViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    private val _error = MutableSharedFlow<CEHModel>()
    val error: SharedFlow<CEHModel> = _error

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _error.emit(CoroutineException.checkThrowableAtViewModel(throwable))
        }
    }

    val bookReports: StateFlow<PagingData<BookReport>> =
        bookReportRepository.getBookReportPaging()
            .cachedIn(viewModelScope + exceptionHandler)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())
}
