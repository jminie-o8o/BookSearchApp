package com.stark.booksearchapp.data.repository

import androidx.paging.PagingData
import com.stark.booksearchapp.data.model.BookReport
import kotlinx.coroutines.flow.Flow

interface BookReportRepository {

    suspend fun getBookReportDetail(isbn: String) : BookReport

    suspend fun insertBookReport(bookReport: BookReport)

    suspend fun deleteBookReport(bookReport: BookReport)

    // For Test
    fun getBookReport(): Flow<List<BookReport>>

    fun getBookReportPaging(): Flow<PagingData<BookReport>>
}
