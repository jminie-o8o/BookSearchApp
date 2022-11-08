package com.stark.booksearchapp.ui.viewmodel

import com.google.common.truth.Truth
import com.stark.booksearchapp.data.model.BookReport
import com.stark.booksearchapp.data.repository.FakeBookReportRepository
import com.stark.booksearchapp.ui.view.bookreport.viewmodel.BookReportDetailViewModel
import com.stark.booksearchapp.ui.view.bookreport.viewmodel.BookReportRegisterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class BookReportDetailViewModelTest {

    private lateinit var registerViewModel: BookReportRegisterViewModel
    private lateinit var detailViewModel: BookReportDetailViewModel

    // 프로덕션 코드에서는 ViewModel 을 싱글톤으로 만들어야하지만
    // 테스트 코드에서는 그럴 필요가 없다.
    @Before
    fun setUp() {
        registerViewModel = BookReportRegisterViewModel(FakeBookReportRepository())
        detailViewModel = BookReportDetailViewModel(FakeBookReportRepository())
    }

    @Test
    @ExperimentalCoroutinesApi
    fun get_book_report_detail_test() = runTest {
        val sampleBookReport = BookReport("a", "b", "c", "d",
            "e", "f", "g", "h")

        detailViewModel.getBookReportDetailTest()
        val bookReportDetail = detailViewModel.bookReportDetailList.first()

        Truth.assertThat(bookReportDetail).isEqualTo(sampleBookReport)
    }
}
