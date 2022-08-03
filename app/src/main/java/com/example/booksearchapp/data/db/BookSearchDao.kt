package com.example.booksearchapp.data.db

import androidx.room.*
import com.example.booksearchapp.data.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 만약 동일한 PrimaryKey 가 있을 경우 덮어쓰기
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books")
    fun getFavoriteBooks(): Flow<List<Book>>
}
