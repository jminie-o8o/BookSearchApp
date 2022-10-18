package com.stark.booksearchapp.ui.viewmodel

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import com.stark.booksearchapp.data.model.Book
import com.stark.booksearchapp.data.repository.FakeBookSearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

// BookViewModel 은 안드로이드 의존성이 없기 때문에 로컬 Unit 테스트를 수행할 수 있다.
// UI Layer 와 Data Layer 두 계층에 걸친 통합 테스트이기 때문에 MediumTest 로 분리해준다.
@MediumTest
class BookViewModelTest {

    private lateinit var viewModel: BookViewModel

    // 프로덕션 코드에서는 ViewModel 을 싱글턴으로 만들어주지만
    // Test 에서는 그렇게 할 필요는 없다.
    // 생성자로 BookSearchRepository 를 전달을 해줘야 하는데
    // 이때 Test 를 위한 FakeBookSearchRepository 를 전달해 준다.
    @Before
    fun setUp() {
        viewModel = BookViewModel(FakeBookSearchRepository())
    }

    // runTest 안에서 임시 객체를 만들어서 ViewModel 의 saveBook 을 통해서 Repository 에 저장
    // Repository 에 저장한 내용을 iewModel.favoriteBooks.first() 을 통해 가져와 contains 을 통해 테스트
    // 마찬가지로 runTest 가 실험중인 메서드이기 때문에 @ExperimentalCoroutinesApi 를 붙여준다.
    @Test
    @ExperimentalCoroutinesApi
    fun insert_book_to_db() = runTest {
        val book = Book(
            listOf("a"), "b", "c",  "d", 0, "e", 0,
            "f", "g", "h", listOf("i"), "j"
        )
        viewModel.saveBook(book)

        val favoriteBooks = viewModel.favoriteBooks.first()
        Truth.assertThat(favoriteBooks).contains(book)
    }
}
