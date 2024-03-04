package com.donghyeon.dev.calculator.ratio

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType

data class RatioState(
    val type: RatioType = RatioType.RATIO,
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
        val select: RatioValue = RatioValue.VALUE1,
        val valueList: List<TextFieldValue>,
        val result: String = "",
    ) {
        fun getValue(): TextFieldValue = getValue(select)

        fun getValue(select: RatioValue): TextFieldValue = valueList[select.index]
    }

    fun getCalculate(): Calculate = getCalculate(type)

    fun getCalculate(type: RatioType): Calculate = calculateList[type.index]
}

enum class RatioValue(
    val index: Int,
    val value: String,
) {
    VALUE1(
        index = 0,
        value = RatioKey.Value1.value,
    ),
    VALUE2(
        index = 1,
        value = RatioKey.Value2.value,
    ),
    VALUE3(
        index = 2,
        value = RatioKey.Value3.value,
    ),
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
