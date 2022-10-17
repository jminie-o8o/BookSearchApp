package com.stark.booksearchapp.data.repository

import androidx.paging.PagingData
import com.stark.booksearchapp.data.model.BookReport
import kotlinx.coroutines.flow.Flow

interface BookReportRepository {

    // Room
    suspend fun getBookReportDetail(isbn: String) : BookReport

    suspend fun insertBookReport(bookReport: BookReport)

    suspend fun deleteBookReport(bookReport: BookReport)

    fun getBookReport(): Flow<List<BookReport>> // Test 용

    // Paging
    fun getBookReportPaging(): Flow<PagingData<BookReport>>
}
