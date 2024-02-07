package com.donghyeon.dev.calculator.percent

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.PercentCalculateType

data class PercentState(
    val type: PercentCalculateType = PercentCalculateType.TYPE1,
    val calculate1: Calculate = Calculate(),
    val calculate2: Calculate = Calculate(),
    val calculate3: Calculate = Calculate(),
    val calculate4: Calculate = Calculate(),
) {
    data class Calculate(
        val select: PercentValueSelect = PercentValueSelect.VALUE1,
        val value1: TextFieldValue = TextFieldValue(),
        val value2: TextFieldValue = TextFieldValue(),
        val result: String = "?",
    )
}

enum class PercentValueSelect(val value: String) {
    VALUE1(PercentKey.Value1.value),
    VALUE2(PercentKey.Value2.value),
}

sealed class PercentKey(val value: String) {
    data object One : PercentKey("1")

    data object Two : PercentKey("2")

    data object Three : PercentKey("3")

    data object Four : PercentKey("4")

    data object Five : PercentKey("5")

    data object Six : PercentKey("6")

    data object Seven : PercentKey("7")

    data object Eight : PercentKey("8")

    data object Nine : PercentKey("9")

    data object Zero : PercentKey("0")

    data object Decimal : PercentKey(".")

    data object Value1 : PercentKey("V1")

    data object Value2 : PercentKey("V2")

    data object Clear : PercentKey("C")

    data object Backspace : PercentKey(R.drawable.ic_backspace_24px.toString())

    data object Left : PercentKey(R.drawable.ic_left_24px.toString())

    data object Right : PercentKey(R.drawable.ic_right_24px.toString())

    data object Copy : PercentKey(R.drawable.ic_copy_24px.toString())
}
