package com.stark.booksearchapp.di

import android.content.Context
import androidx.room.Room
import com.stark.booksearchapp.data.db.BookSearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    // 테스트용 BookSearchDatabase 는 테스트가 끝나면 바로 사라질 일회용 객체이기 때문에
    // 프로덕션 코드 때 처럼 Singleton 으로 설정해주지 않아도 된다.
    // 그리고 프로덕션 코드에도 BookSearchDatabase 를 제공해주는 provideBookSearchDatabase 가 존재하기 때문에
    // Hilt 가 BookSearchDatabase 를 주입해야 할 곳에 어떤 것을 주입해야 할 지 헷갈리게 된다.
    // 따라서 이를 방지하기 위해 @Named 어노테이션을 통해 Hilt 가 객체를 구분할 수 있도록 해준다.
    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context): BookSearchDatabase =
        // database 는 inMemoryDatabaseBuilder 를 사용해서 메모리 안에서만 생성하고 테스트가 끝나면 파괴
        // 또한 Room 은 ANR 을 방지하기 위해 Main 스레드에서 쿼리를 금지하고 있는데
        // 데이터베이스에서의 쿼리를 멀티스레드에서 수행하면 결과를 예측할 수 없기 때문에
        // 테스트에서는 allowMainThreadQueries 를 통해 Main 스레드에서의 수행을 명시적으로 허가해준다.
        Room.inMemoryDatabaseBuilder(
            context,
            BookSearchDatabase::class.java
        ).allowMainThreadQueries().build()
}
