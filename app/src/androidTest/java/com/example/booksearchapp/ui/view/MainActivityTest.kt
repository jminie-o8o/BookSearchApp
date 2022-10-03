package com.example.booksearchapp.ui.view

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    // ActivityScenario 를 사용하면 테스트용 Activity 를 손쉽게 생성 가능
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        activityScenario.close()
    }


    @Test
    @SmallTest
    fun test_activity_state() {
        val activityState = activityScenario.state.name
        assertThat(activityState).isEqualTo("RESUMED")
    }
}
