package com.stark.booksearchapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stark.booksearchapp.data.model.BookReport

@Database(
    entities = [BookReport::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(OrmConverter::class)
abstract class BookReportDatabase : RoomDatabase() {

    abstract fun bookReportDao(): BookReportDao
}
