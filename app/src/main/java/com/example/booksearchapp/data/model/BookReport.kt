package com.example.booksearchapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookReports")
data class BookReport(
    @PrimaryKey(autoGenerate = false)
    val isbn: String,
    val thumbnail: String,
    val title: String,
    val author: String,
    val publisher: String,
    val reportTitle: String,
    val reportContents: String
)
