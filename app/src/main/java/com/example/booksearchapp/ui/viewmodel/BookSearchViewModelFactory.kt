package com.example.booksearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.booksearchapp.data.repository.BookSearchRepositoryImpl

@Suppress("UNCHECKED_CAST")
class BookSearchViewModelFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BookSearchViewModel::class.java) -> BookSearchViewModel(BookSearchRepositoryImpl()) as T
            else -> throw IllegalAccessException("Failed to create ViewModel")
        }
    }
}