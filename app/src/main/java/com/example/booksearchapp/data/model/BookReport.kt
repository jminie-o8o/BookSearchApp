package com.example.booksearchapp.data.model

import androidx.room.Entity

@Entity(tableName = "bookReports")
data class BookReport(
    val id: Int,
    val thumbnail: String,
    val title: String,
    val author: String,
    val publisher: String,
    val reportTitle: String,
    val reportContents: String
)
