package com.stark.booksearchapp.data.repository

import androidx.paging.PagingData
import com.stark.booksearchapp.data.model.BookReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// 테스트에는 사용할 수 있지만 프로덕션에는 사용할 수 없는 Fake-Test-Double
class FakeBookReportRepository : BookReportRepository {

    private val reportItems = mutableListOf<BookReport>()

    override suspend fun getBookReportDetail(isbn: String): BookReport {
        return BookReport(isbn, "b", "c", "d",
            "e", "f", "g", "h")
    }

    override suspend fun insertBookReport(bookReport: BookReport) {
        reportItems.add(bookReport)
    }

    override suspend fun deleteBookReport(bookReport: BookReport) {
        reportItems.remove(bookReport)
    }

    override fun getBookReport(): Flow<List<BookReport>> {
        return flowOf(reportItems)
    }

    override fun getBookReportPaging(): Flow<PagingData<BookReport>> {
        TODO("Not yet implemented")
    }
}
