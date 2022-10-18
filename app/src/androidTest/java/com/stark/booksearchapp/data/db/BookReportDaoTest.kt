package com.stark.booksearchapp.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.stark.booksearchapp.data.model.BookReport
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class BookReportDaoTest {

    private lateinit var database: BookReportDatabase
    private lateinit var dao: BookReportDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BookReportDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.bookReportDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    @ExperimentalCoroutinesApi
    fun insert_book_report_to_db() = runTest {
        val insertBookReport = BookReport("a", "b", "c", "d",
        "e", "f", "g", "h")

        dao.insertBookReport(insertBookReport)

        val getBookReport = dao.getBookReports().first()
        assertThat(getBookReport).contains(insertBookReport)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun delete_book_report_from_db() = runTest {
        val insertBookReport = BookReport("a", "b", "c", "d",
            "e", "f", "g", "h")

        dao.insertBookReport(insertBookReport)
        dao.deleteBookReport(insertBookReport)

        val getBookReport = dao.getBookReports().first()
        assertThat(getBookReport).doesNotContain(insertBookReport)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun get_book_report_from_db() = runTest {
        val insertBookReport = BookReport("a", "b", "c", "d",
            "e", "f", "g", "h")

        dao.insertBookReport(insertBookReport)
        val target = dao.getBookReportDetail("a")

        assertThat(insertBookReport).isEqualTo(target)
    }
}
