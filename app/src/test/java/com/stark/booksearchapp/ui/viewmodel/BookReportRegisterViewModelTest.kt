package com.stark.booksearchapp.ui.viewmodel

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.repository.FakeBookReportRepository
import com.stark.booksearchapp.ui.bookreport.viewmodel.BookReportRegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@MediumTest
class BookReportRegisterViewModelTest {

    private lateinit var viewModel: BookReportRegisterViewModel

    // 프로덕션 코드에서는 ViewModel 을 싱글톤으로 만들어야하지만
    // 테스트 코드에서는 그럴 필요가 없다.
    @Before
    fun setUp() {
        viewModel = BookReportRegisterViewModel(FakeBookReportRepository())
    }

    @Test
    @ExperimentalCoroutinesApi
    fun save_book_report_test() = runTest {
        val saveBookReport = BookReport("a", "b", "c", "d",
            "e", "f", "g", "h")

        viewModel.saveBookReport(saveBookReport)

        val getBookReport = viewModel.bookReportsForTest.first()
        assertThat(getBookReport).contains(saveBookReport)
    }
}
