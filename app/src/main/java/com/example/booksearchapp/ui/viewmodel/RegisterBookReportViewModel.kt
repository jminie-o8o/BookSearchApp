package com.example.booksearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booksearchapp.data.model.Book
import com.example.booksearchapp.data.repository.BookReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterBookReportViewModel @Inject constructor(
    private val bookReportRepository: BookReportRepository
) : ViewModel() {

    // Room
    fun saveBookReport(book: Book) = viewModelScope.launch(Dispatchers.IO) {

    }
}
