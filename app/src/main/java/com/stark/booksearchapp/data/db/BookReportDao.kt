package com.stark.booksearchapp.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stark.booksearchapp.data.model.BookReport
import kotlinx.coroutines.flow.Flow

@Dao
interface BookReportDao {

    @Query("SELECT * FROM bookReports WHERE isbn = :isbn")
    suspend fun getBookReportDetail(isbn: String): BookReport

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookReport(bookReport: BookReport)

    @Delete
    suspend fun deleteBookReport(bookReport: BookReport)

    @Query("SELECT * FROM bookReports")
    fun getBookReports(): Flow<List<BookReport>>

    @Query("SELECT * FROM bookReports")
    fun getBookReportsPaging(): PagingSource<Int, BookReport> // Room 은 쿼리 결과를 PagingSource 타입으로 반환받을 수 있다.
}
