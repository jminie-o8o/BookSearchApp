package com.example.booksearchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.booksearchapp.data.db.BookReportDatabase
import com.example.booksearchapp.data.model.BookReport
import com.example.booksearchapp.util.Constants.PAGING_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookReportRepositoryImpl @Inject constructor(
    private val db: BookReportDatabase
): BookReportRepository {
    override suspend fun getBookReportDetail(isbn: String): BookReport {
        return db.bookReportDao().getBookReportDetail(isbn)
    }

    override suspend fun insertBookReport(bookReport: BookReport) {
        db.bookReportDao().insertBookReport(bookReport)
    }

    override suspend fun deleteBookReport(bookReport: BookReport) {
        db.bookReportDao().deleteBookReport(bookReport)
    }

    override fun getBookReport(): Flow<List<BookReport>> {
        return db.bookReportDao().getFavoriteBooks()
    }

    override fun getBookReportPaging(): Flow<PagingData<BookReport>> {
        val pagingFactory = {
            db.bookReportDao().getFavoritePagingBooks()
        }

        return Pager(
            config = PagingConfig(
                pageSize = PAGING_SIZE,
                enablePlaceholders = false,
                maxSize = PAGING_SIZE * 3
            ),
            pagingSourceFactory = pagingFactory
        ).flow
    }
}
