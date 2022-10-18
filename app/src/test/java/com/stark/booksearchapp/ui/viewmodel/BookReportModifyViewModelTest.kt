package com.stark.booksearchapp.ui.viewmodel

import com.google.common.truth.Truth
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.repository.FakeBookReportRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class BookReportModifyViewModelTest {

    private lateinit var viewModel: BookReportModifyViewModel

    // 프로덕션 코드에서는 ViewModel 을 싱글톤으로 만들어야하지만
    // 테스트 코드에서는 그럴 필요가 없다.
    @Before
    fun setUp() {
        viewModel = BookReportModifyViewModel(FakeBookReportRepository())
    }

    @Test
    @ExperimentalCoroutinesApi
    fun save_book_report_test() = runTest {
        val saveBookReport = BookReport("a", "b", "c", "d",
            "e", "f", "g", "h")

        viewModel.saveBookReport(saveBookReport)

        val getBookReport = viewModel.bookReportsForTest.first()
        Truth.assertThat(getBookReport).contains(saveBookReport)
    }
}