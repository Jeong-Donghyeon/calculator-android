package com.donghyeon.dev.calculator.percent

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.PercentType

data class PercentState(
    val type: PercentType? = null,
    val v1Focus: Boolean = false,
    val v2Focus: Boolean = false,
    val calculateList: List<Calculate> =
        List(
            size = PercentType.entries.count(),
            init = {
                Calculate(
                    valueList =
                        List(
                            size = 2,
                            init = { TextFieldValue() },
                        ),
                )
            },
        ),
) {
    data class Calculate(
        val valueList: List<TextFieldValue>,
        val result: String = "?",
    )

    fun getCalculate(): Calculate {
        return calculateList[type?.ordinal ?: 0]
    }

    fun getValueIndex(): Int {
        return if (!v1Focus && v2Focus) {
            1
        } else {
            0
        }
    }

    fun getValue(): TextFieldValue {
        return getCalculate().valueList[getValueIndex()]
    }
}

sealed class PercentKey(val value: String) {
    data object Clear : PercentKey("C")

    data object Left : PercentKey(R.drawable.ic_left_24px.toString())

    data object Right : PercentKey(R.drawable.ic_right_24px.toString())

    data object Backspace : PercentKey(R.drawable.ic_backspace_24px.toString())

    data object Copy : PercentKey(R.drawable.ic_copy_24px.toString())

    data class Paste(val result: String) : PercentKey(R.drawable.ic_paste_24px.toString())

    data object Enter : PercentKey(R.drawable.ic_tab_24px.toString())

    data object ZeroZero : PercentKey("00")

    data object Zero : PercentKey("0")

    data object Decimal : PercentKey(".")

    data object One : PercentKey("1")

    data object Two : PercentKey("2")

    data object Three : PercentKey("3")

    data object Four : PercentKey("4")

    data object Five : PercentKey("5")

    data object Six : PercentKey("6")

    data object Seven : PercentKey("7")

    data object Eight : PercentKey("8")

    data object Nine : PercentKey("9")
}
