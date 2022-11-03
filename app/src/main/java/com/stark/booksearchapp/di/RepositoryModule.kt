package com.stark.booksearchapp.di

import com.stark.booksearchapp.data.repository.AlarmRepository
import com.stark.booksearchapp.data.repository.AlarmRepositoryImpl
import com.stark.booksearchapp.data.repository.BookReportRepository
import com.stark.booksearchapp.data.repository.BookReportRepositoryImpl
import com.stark.booksearchapp.data.repository.BookSearchRepository
import com.stark.booksearchapp.data.repository.BookSearchRepositoryImpl
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

    @Singleton
    @Binds
    abstract fun bindAlarmRepository(
        alarmRepositoryImpl: AlarmRepositoryImpl,
    ): AlarmRepository
}
