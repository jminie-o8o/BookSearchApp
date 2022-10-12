package com.example.booksearchapp.di

import com.example.booksearchapp.data.repository.BookReportRepository
import com.example.booksearchapp.data.repository.BookReportRepositoryImpl
import com.example.booksearchapp.data.repository.BookSearchRepository
import com.example.booksearchapp.data.repository.BookSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindBookSearchRepository(
        bookSearchRepositoryImpl: BookSearchRepositoryImpl,
    ): BookSearchRepository

    @Singleton
    @Binds
    abstract fun bindBookReportRepository(
        bookReportRepositoryImpl: BookReportRepositoryImpl,
    ): BookReportRepository
}
