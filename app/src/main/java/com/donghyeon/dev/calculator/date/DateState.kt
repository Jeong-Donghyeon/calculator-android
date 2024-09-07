package com.donghyeon.dev.calculator.date

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.DateType

data class DateState(
    val type: DateType? = null,
    val hint: String = "",
)

data class DateDayDateState(
    val focus: Focus = Focus.DATE,
    val date: TextFieldValue = TextFieldValue(),
    val day: TextFieldValue = TextFieldValue(),
    val agoLater: Boolean = true,
    val result: String = "?",
) {
    enum class Focus {
        DATE,
        DAY,
        AGO_LATER,
    }
}

data class DateDateDayState(
    val date1: TextFieldValue = TextFieldValue(),
    val date1Focus: Boolean = true,
    val date2: TextFieldValue = TextFieldValue(),
    val date2Focus: Boolean = false,
    val result: String = "?",
)

sealed class DateKey(val value: String) {
    data object Clear : DateKey("C")

    data object Left : DateKey(R.drawable.ic_left_24px.toString())

    data object Right : DateKey(R.drawable.ic_right_24px.toString())

    data object Backspace : DateKey(R.drawable.ic_backspace_24px.toString())

    data object Copy : DateKey(R.drawable.ic_copy_24px.toString())

    data class Paste(val result: String) : DateKey(R.drawable.ic_paste_24px.toString())

    data object Enter : DateKey(R.drawable.ic_tab_24px.toString())

    data object Today : DateKey(R.drawable.ic_today_24px.toString())

    data object ZeroZero : DateKey("00")

    data object Zero : DateKey("0")

    data object One : DateKey("1")

    data object Two : DateKey("2")

    data object Three : DateKey("3")

    data object Four : DateKey("4")

    data object Five : DateKey("5")

    data object Six : DateKey("6")

    data object Seven : DateKey("7")

    data object Eight : DateKey("8")

    data object Nine : DateKey("9")
}
