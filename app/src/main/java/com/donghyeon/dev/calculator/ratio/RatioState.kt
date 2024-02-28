package com.donghyeon.dev.calculator.ratio

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType

data class RatioState(
    val type: RatioType = RatioType.RATIO,
    val ratio: Ratio = Ratio(),
    val simplify: Simplify = Simplify(),
) {
    data class Ratio(
        val value1: TextFieldValue = TextFieldValue(),
        val value2: TextFieldValue = TextFieldValue(),
        val value3: TextFieldValue = TextFieldValue(),
        val result: String = "",
    )

    data class Simplify(
        val value1: TextFieldValue = TextFieldValue(),
        val value2: TextFieldValue = TextFieldValue(),
        val result1: String = "",
        val result2: String = "",
    )
}

sealed class RatioKey(val value: String) {
    data object One : RatioKey("1")

    data object Two : RatioKey("2")

    data object Three : RatioKey("3")

    data object Four : RatioKey("4")

    data object Five : RatioKey("5")

    data object Six : RatioKey("6")

    data object Seven : RatioKey("7")

    data object Eight : RatioKey("8")

    data object Nine : RatioKey("9")

    data object Zero : RatioKey("0")

    data object ZeroZero : RatioKey("00")

    data object Decimal : RatioKey(".")

    data object Value1 : RatioKey("V1")

    data object Value2 : RatioKey("V2")

    data object Value3 : RatioKey("V3")

    data object Clear : RatioKey("C")

    data object Backspace : RatioKey(R.drawable.ic_backspace_24px.toString())

    data object Left : RatioKey(R.drawable.ic_left_24px.toString())

    data object Right : RatioKey(R.drawable.ic_right_24px.toString())
}
