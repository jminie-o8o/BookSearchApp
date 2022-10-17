package com.stark.booksearchapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "bookReports")
data class BookReport(
    @PrimaryKey(autoGenerate = false)
    val isbn: String,
    val thumbnail: String,
    val title: String,
    val author: String,
    val publisher: String,
    val reportTitle: String,
    val reportContents: String,
    val date: String
): Parcelable
