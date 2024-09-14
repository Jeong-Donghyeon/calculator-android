package com.donghyeon.dev.calculator.calculate.date

import com.donghyeon.dev.calculator.calculate.DateUseCase
import org.junit.Test

class DateTest {

    private val dateUseCase = DateUseCase()

    @Test
    fun date() {
        val result =
            dateUseCase.date(
                date = "20240605",
                day = "365",
                agoLater = true,
            )
        println(result)
    }

    @Test
    fun day() {
        val result =
            dateUseCase.day(
                date1 = "20240605",
                date2 = "20241204",
            )
        println(result)
    }
}
