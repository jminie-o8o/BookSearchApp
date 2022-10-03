package com.example.booksearchapp.util

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@SmallTest
class CalculatorTest {

    private lateinit var calculator: Calculator

    // Before 어노테이션은 각 테스트에 실행 직전에 수행되어야 하는 작업을 수행한다.
    // 여기서는 Calculator 인스턴스를 새로 만드는 작업을 정의하였다.
    @Before
    fun setUp() {
        calculator = Calculator()
    }

    // After 어노테이션은 테스트 종료 직후에 수행되어야 하는 작업을 수행한다.
    @After
    fun tearDown() {

    }

    @Test
    fun `더하기 테스트`() {
        // Given
        val x = 4
        val y = 2

        // When
        val result = calculator.addition(x, y)

        // Then
        assertThat(result).isEqualTo(6)
    }

    @Test
    fun `빼기 테스트`() {
        // Given
        val x = 4
        val y = 2

        // When
        val result = calculator.subtraction(x, y)

        // Then
        assertThat(result).isEqualTo(2)
    }

    @Test
    fun `곱하기 테스트`() {
        // Given
        val x = 4
        val y = 2

        // When
        val result = calculator.multiplication(x, y)

        // Then
        assertThat(result).isEqualTo(8)
    }

    @Test
    fun `나누기 테스트`() {
        // Given
        val x = 4
        val y = 2

        // When
        val result = calculator.division(x, y)

        // Then
        assertThat(result).isEqualTo(2)
    }
}
