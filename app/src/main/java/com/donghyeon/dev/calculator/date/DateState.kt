package com.donghyeon.dev.calculator.date

import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.DateType

data class DateState(
    val type: DateType = DateType.DATE_SEARCH,
)

sealed class DateKey(val value: String) {
    data object One : DateKey("1")

    data object Two : DateKey("2")

    data object Three : DateKey("3")

    data object Four : DateKey("4")

    data object Five : DateKey("5")

    data object Six : DateKey("6")

    data object Seven : DateKey("7")

    data object Eight : DateKey("8")

    data object Nine : DateKey("9")

    data object Zero : DateKey("0")

    data object ZeroZero : DateKey("00")

    data object Decimal : DateKey(".")

    data object Clear : DateKey("C")

    data object Backspace : DateKey(R.drawable.ic_backspace_24px.toString())

    data object Left : DateKey(R.drawable.ic_left_24px.toString())

    data object Right : DateKey(R.drawable.ic_right_24px.toString())

    data object Copy : DateKey(R.drawable.ic_copy_24px.toString())

    data object Past : DateKey(R.drawable.ic_paste_24px.toString())

    data object Next : DateKey(R.drawable.ic_next_24px.toString())
}
