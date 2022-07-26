package com.stark.booksearchapp.util

import com.stark.booksearchapp.BuildConfig

object Constants {
    const val BASE_URL = "https://dapi.kakao.com/"
    const val API_KEY = BuildConfig.bookApiKey
    const val SEARCH_BOOKS_TIME_DELAY = 400L
    const val DATASTORE_NAME = "preferences_datastore"
    const val ALARM_DATASTORE = "alarm_datastore"
    const val PAGING_SIZE = 15
}
