package com.example.booksearchapp.util

import com.example.booksearchapp.BuildConfig

object Constants {
    const val BASE_URL = "https://dapi.kakao.com/"
    const val API_KEY = BuildConfig.bookApiKey
    const val SEARCH_BOOKS_TIME_DELAY = 400L
    const val DATASTORE_NAME = "preferences_datastore"
    const val PAGING_SIZE = 15 // 페이징에서 한번에 가져올 데이터 수
}
