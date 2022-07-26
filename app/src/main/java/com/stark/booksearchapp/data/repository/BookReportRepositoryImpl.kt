package com.stark.booksearchapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stark.booksearchapp.data.db.BookReportDatabase
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.util.Constants.PAGING_SIZE
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
        return db.bookReportDao().getBookReports()
    }

    override fun getBookReportPaging(): Flow<PagingData<BookReport>> {
        val pagingFactory = {
            db.bookReportDao().getBookReportsPaging()
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
