package com.stark.booksearchapp.data.db

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.stark.booksearchapp.data.model.Book
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

// BookSearchDaoTest 에 Runner 를 교체해서 Hilt 의 EntryPoint 로 만들어준다.
@HiltAndroidTest
@SmallTest
class BookSearchDaoTest {

    @Inject
    @Named("test_db")
    lateinit var database: BookSearchDatabase
    private lateinit var dao: BookSearchDao

    // Hilt 컴포넌트의 상태와 주입을 관리하는 HiltAndroidRule() 을 테스트에 추가해준다.
    // @Before 에서 inject() 메서드를 실행하면 주입 설정한 모든 의존성을 Hilt 가 한번에 주입을 해주게 된다.
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        dao = database.bookSearchDao()
    }

    @After
    fun tearDown() {
        // After 에서 database 파괴
        database.close()
    }

    // 코루틴 테스트는 runTest 내부에서 수행하면 된다.
    // runTest 는 아직 실험중인 API 라서 @ExperimentalCoroutinesApi 를 붙여주도록 한다.
    @Test
    @ExperimentalCoroutinesApi
    fun insert_book_to_db() = runTest {
        val book = Book(
            listOf("a"), "b", "c",  "d", 0, "e", 0,
            "f", "g", "h", listOf("i"), "j"
        )
        dao.insertBook(book)

        val favoriteBooks = dao.getFavoriteBooks().first()
        assertThat(favoriteBooks).contains(book)
    }

    // 삭제 로직은 book 객체를 추가한 다음 바로 삭제하고
    // doesNotContain 을 통해 book 객체가 제대로 삭제되었는지 확인한다.
    @Test
    @ExperimentalCoroutinesApi
    fun delete_book_to_db() = runTest {
        val book = Book(
            listOf("a"), "b", "c",  "d", 0, "e", 0,
            "f", "g", "h", listOf("i"), "j"
        )
        dao.insertBook(book)
        dao.deleteBook(book)

        val favoriteBooks = dao.getFavoriteBooks().first()
        assertThat(favoriteBooks).doesNotContain(book)
    }
}
