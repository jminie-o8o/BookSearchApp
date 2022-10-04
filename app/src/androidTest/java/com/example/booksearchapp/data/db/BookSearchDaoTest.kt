package com.example.booksearchapp.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.booksearchapp.data.model.Book
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class BookSearchDaoTest {

    private lateinit var database: BookSearchDatabase
    private lateinit var dao: BookSearchDao

    @Before
    fun setUp() {
        // Before 에서 database 와 dao 를 생성
        // database 는 inMemoryDatabaseBuilder 를 사용해서 메모리 안에서만 생성하고 테스트가 끝나면 파괴
        // 또한 Room 은 ANR 을 방지하기 위해 Main 스레드에서 쿼리를 금지하고 있는데
        // 데이터베이스에서의 쿼리를 멀티스레드에서 수행하면 결과를 예측할 수 없기 때문에
        // 테스트에서는 allowMainThreadQueries 를 통해 Main 스레드에서의 수행을 명시적으로 허가해준다.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BookSearchDatabase::class.java
        ).allowMainThreadQueries().build()
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
