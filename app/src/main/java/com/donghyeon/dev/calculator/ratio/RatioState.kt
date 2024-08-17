package com.donghyeon.dev.calculator.ratio

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType

data class RatioState(
    val type: RatioType? = null,
    val v1Focus: Boolean = false,
    val v2Focus: Boolean = false,
    val v3Focus: Boolean = false,
    val calculateList: List<Calculate> =
        List(
            size = RatioType.entries.count(),
            init = {
                Calculate(
                    valueList =
                        List(
                            size = if (it == 0) 3 else 2,
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
        return if (!v1Focus && v2Focus && !v3Focus) {
            1
        } else if (!v1Focus && !v2Focus && v3Focus) {
            2
        } else {
            0
        }
    }

    fun getValue(): TextFieldValue {
        return getCalculate().valueList[getValueIndex()]
    }
}

sealed class RatioKey(val value: String) {
    data object Clear : RatioKey("C")

    data object Left : RatioKey(R.drawable.ic_left_24px.toString())

    data object Right : RatioKey(R.drawable.ic_right_24px.toString())

    data object Backspace : RatioKey(R.drawable.ic_backspace_24px.toString())

    data object Copy : RatioKey(R.drawable.ic_copy_24px.toString())

    data class Paste(val result: String) : RatioKey(R.drawable.ic_paste_24px.toString())

    data object Enter : RatioKey(R.drawable.ic_tab_24px.toString())

    data object ZeroZero : RatioKey("00")

    data object Zero : RatioKey("0")

    data object Decimal : RatioKey(".")

    data object One : RatioKey("1")

    data object Two : RatioKey("2")

    data object Three : RatioKey("3")

    data object Four : RatioKey("4")

    data object Five : RatioKey("5")

    data object Six : RatioKey("6")

    data object Seven : RatioKey("7")

    data object Eight : RatioKey("8")

    data object Nine : RatioKey("9")
}
