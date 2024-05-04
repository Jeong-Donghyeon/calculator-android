package com.donghyeon.dev.calculator.date

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.DateType

data class DateState(
    val type: DateType? = null,
    val dateSearch: DateSearch = DateSearch(),
    val dateConvert: DateConvert = DateConvert(),
    val timeConvert: TimeConvert = TimeConvert(),
) {
    data class DateSearch(
        val date: TextFieldValue = TextFieldValue(),
        val day: TextFieldValue = TextFieldValue(),
        val agoLater: Boolean = false,
        val result: String = "?",
    )

    data class DateConvert(
        val date1: TextFieldValue = TextFieldValue(),
        val date2: TextFieldValue = TextFieldValue(),
        val result: String = "?",
    )

    data class TimeConvert(
        val second: TextFieldValue = TextFieldValue(),
        val minute: TextFieldValue = TextFieldValue(),
        val hour: TextFieldValue = TextFieldValue(),
        val day: TextFieldValue = TextFieldValue(),
        val result: String = "?",
    )
}

sealed class DateKey(val value: String) {
    data object Clear : DateKey("C")

    data object Left : DateKey(R.drawable.ic_left_24px.toString())

    data object Right : DateKey(R.drawable.ic_right_24px.toString())

    data object Backspace : DateKey(R.drawable.ic_backspace_24px.toString())

    data object Copy : DateKey(R.drawable.ic_copy_24px.toString())

    data class Paste(val result: String) : DateKey(R.drawable.ic_paste_24px.toString())

    data object Enter : DateKey(R.drawable.ic_tab_24px.toString())

    data object Unit : DateKey(R.drawable.ic_code_24px.toString())

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
