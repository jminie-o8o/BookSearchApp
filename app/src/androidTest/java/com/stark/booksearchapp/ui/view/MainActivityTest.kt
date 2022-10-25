package com.stark.booksearchapp.ui.view

import android.view.View
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.stark.booksearchapp.R
import com.stark.booksearchapp.ui.adapter.BookReportPagingAdapter
import com.stark.booksearchapp.ui.adapter.BookSearchAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var activityScenarioRule : ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    @SmallTest
    fun test_activity_state() {
        val activityState = activityScenarioRule.scenario.state.name
        assertThat(activityState).isEqualTo("RESUMED")
    }

    // 책 검색과 관심목록 추가 및 삭제 Test
    @Test
    @LargeTest
    fun from_SearchFragment_to_FavoriteFragment_Ui_Operation() {
        // 1. SearchFragment
        // 1-1. "NO Result" 가 잘 출력되는지 확인
        onView(withId(R.id.tv_emptylist))
            .check(matches(withText("원하는 책을 검색해보세요:)")))
        // 1-2. 검색어로 "android" 입력
        onView(withId(R.id.et_search))
            .perform(typeText("android"))
        onView(isRoot()).perform(waitFor(3000))
        // 1-3. 리사이클러뷰 표시 확인
        onView(withId(R.id.rv_search_result))
            .check(matches(isDisplayed()))
        // 1-4. 첫 번째 반환 값 클릭
        onView(withId(R.id.rv_search_result))
            .perform(actionOnItemAtPosition<BookSearchAdapter.BookSearchViewHolder>(0, click()))
        onView(isRoot()).perform(waitFor(1000))
        // 1-5. BookFragment 결과를 저장
        onView(withId(R.id.fab_favorite))
            .perform(click())
        // 1-6. 이전 화면으로 돌아감
        pressBack()
        // 1-7. SnackBar 가 사라질 때까지 대기
        onView(isRoot()).perform(waitFor(3000))
        onView(withId(R.id.rv_search_result))
            .check(matches(isDisplayed()))

        // 2. FavoriteFragment
        // 2-1. FavoriteFragment 로 이동
        onView(withId(R.id.fragment_favorite))
            .perform(click())
        // 2-2. 리사이클러뷰 표시 확인
        onView(withId(R.id.rv_favorite_books))
            .perform(click())
        // 2-3. 첫번째 아이템 슬라이드하여 삭제
        onView(withId(R.id.rv_favorite_books))
            .perform(actionOnItemAtPosition<BookSearchAdapter.BookSearchViewHolder>(0 , swipeLeft()))
    }

    // 책 검색과 독후감 등록 Test
    @Test
    @LargeTest
    fun from_SearchFragment_to_BookReportRegisterFragment_Ui_Operation() {
        // 1. SearchFragment
        // 1-1. 검색어로 "android" 입력
        onView(withId(R.id.et_search))
            .perform(typeText("android"))
        onView(isRoot()).perform(waitFor(3000))
        // 1-2. 리사이클러뷰 표시 확인
        onView(withId(R.id.rv_search_result))
            .check(matches(isDisplayed()))
        // 1-3. 첫 번째 반환 값 클릭
        onView(withId(R.id.rv_search_result))
            .perform(actionOnItemAtPosition<BookSearchAdapter.BookSearchViewHolder>(0, click()))
        onView(isRoot()).perform(waitFor(1000))

        // 2. RegisterBookReportFragment
        // 2-1. 독후감 쓰러가기 버튼 클릭
        onView(withId(R.id.btn_register_book_report))
            .perform(click())
        // 2-2. 독후감 제목 작성
        onView(withId(R.id.et_book_report_title))
            .perform(typeText("title"))
        // 2-3. 독후감 내용 작성
        onView(withId(R.id.et_book_report_contents))
            .perform(typeText("contents"))
        closeSoftKeyboard()
        // 2-4. 독후감 등록 버튼 클릭
        onView(withId(R.id.btn_register_book_report))
            .perform(click())

        // 3. BookReportDetailFragment
        // 3-1. 독후감 제목 일치 확인
        onView(withId(R.id.tv_book_report_title_detail))
            .check(matches(withText("title")))
        // 3-2. 독후감 내용 일치 확인
        onView(withId(R.id.tv_book_report_contents_detail))
            .check(matches(withText("contents")))
    }

    // BookReportFragment 에서 독후감 RecyclerView 동작 Test
    @Test
    @LargeTest
    fun from_SearchFragment_to_BookReportFragment_Ui_Operation() {
        // 1. SearchFragment
        // 1-1. 검색어로 "android" 입력
        onView(withId(R.id.et_search))
            .perform(typeText("android"))
        onView(isRoot()).perform(waitFor(3000))
        // 1-2. 리사이클러뷰 표시 확인
        onView(withId(R.id.rv_search_result))
            .check(matches(isDisplayed()))
        // 1-3. 첫 번째 반환 값 클릭
        onView(withId(R.id.rv_search_result))
            .perform(actionOnItemAtPosition<BookSearchAdapter.BookSearchViewHolder>(0, click()))
        onView(isRoot()).perform(waitFor(1000))

        // 2. RegisterBookReportFragment
        // 2-1. 독후감 쓰러가기 버튼 클릭
        onView(withId(R.id.btn_register_book_report))
            .perform(click())
        // 2-2. 독후감 제목 작성
        onView(withId(R.id.et_book_report_title))
            .perform(typeText("title"))
        // 2-3. 독후감 내용 작성
        onView(withId(R.id.et_book_report_contents))
            .perform(typeText("contents"))
        closeSoftKeyboard()
        // 2-4. 독후감 등록 버튼 클릭
        onView(withId(R.id.btn_register_book_report))
            .perform(click())

        // 3. BookReportDetailFragment
        // 3-1. 독후감 제목 일치 확인
        onView(withId(R.id.tv_book_report_title_detail))
            .check(matches(withText("title")))
        // 3-2. 독후감 내용 일치 확인
        onView(withId(R.id.tv_book_report_contents_detail))
            .check(matches(withText("contents")))
        pressBack()

        // 4. BookReportFragment
        // 4-1. BookReportFragment 로 이동
        onView(withId(R.id.fragment_book_report))
            .perform(click())
        // 4-2. 리사이클러뷰 표시 확인
        onView(withId(R.id.rv_book_report))
            .check(matches(isDisplayed()))
        onView(isRoot()).perform(waitFor(1000))
        // 4-3. 첫 번째 반환 값 클릭
        onView(withId(R.id.rv_book_report))
            .perform(actionOnItemAtPosition<BookReportPagingAdapter.BookReportViewHolder>(0, click()))
        onView(isRoot()).perform(waitFor(1000))
        // 4-4. 첫 번째 독후감 제목 일치여부 확인
        onView(withId(R.id.tv_book_report_title_detail))
            .check(matches(withText("title")))
        // 4-5. 첫 번째 독후감 내용 일치여부 확인
        onView(withId(R.id.tv_book_report_contents_detail))
            .check(matches(withText("contents")))
    }

    // 검색어로 "android" 를 입력해서 네트워크로 가져오는데 시간이 걸리기 때문에
    // 바로 리사이클러뷰 표시 확인하면 테스트 실패가 발생하기 때문에 waitFor 메서드를 작성해준다.
    private fun waitFor(delay: Long) : ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()

            override fun getDescription(): String = "wait for $delay milliseconds"

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
