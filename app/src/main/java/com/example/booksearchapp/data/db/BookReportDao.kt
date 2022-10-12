package com.example.booksearchapp.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.booksearchapp.data.model.BookReport
import kotlinx.coroutines.flow.Flow

@Dao
interface BookReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookReport(bookReport: BookReport)

    @Delete
    suspend fun deleteBookReport(bookReport: BookReport)

    @Query("SELECT * FROM bookReports")
    fun getFavoriteBooks(): Flow<List<BookReport>>

    @Query("SELECT * FROM bookReports")
    fun getFavoritePagingBooks(): PagingSource<Int, BookReport>
}
