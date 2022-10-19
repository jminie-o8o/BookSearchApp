package com.stark.booksearchapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// Test 앱에 Hilt 를 적용하기 위해서는 AndroidJunitRunner 를 상속받는 새로운 TestRunner 가 필요
class HiltTestRunner : AndroidJUnitRunner(){

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
