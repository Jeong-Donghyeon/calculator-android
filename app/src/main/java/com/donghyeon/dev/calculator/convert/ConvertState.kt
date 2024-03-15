package com.donghyeon.dev.calculator.convert

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.ConvertType

data class ConvertState(
    val type: ConvertType = ConvertType.LENGTH,
    val calculateList: List<Calculate> =
        List(
            size = ConvertType.entries.count(),
            init = { Calculate() },
        ),
) {
    data class Calculate(
        val select: ConvertValue = ConvertValue.VALUE1,
        val valueList: List<TextFieldValue> =
            List(
                size = ConvertValue.entries.count(),
                init = { TextFieldValue() },
            ),
        val result: String = "?",
    ) {
        fun getValue(): TextFieldValue = getValue(select)

        fun getValue(select: ConvertValue): TextFieldValue = valueList[select.index]
    }

    fun getCalculate(): Calculate = getCalculate(type)

    fun getCalculate(type: ConvertType): Calculate = calculateList[type.index]
}

enum class ConvertValue(
    val index: Int,
    val value: String,
) {
    VALUE1(
        index = 0,
        value = ConvertKey.Value1.value,
    ),
    VALUE2(
        index = 1,
        value = ConvertKey.Value2.value,
    ),
}

sealed class ConvertKey(val value: String) {
    data object One : ConvertKey("1")

    data object Two : ConvertKey("2")

    data object Three : ConvertKey("3")

    data object Four : ConvertKey("4")

    data object Five : ConvertKey("5")

    data object Six : ConvertKey("6")

    data object Seven : ConvertKey("7")

    data object Eight : ConvertKey("8")

    data object Nine : ConvertKey("9")

    data object Zero : ConvertKey("0")

    data object ZeroZero : ConvertKey("00")

    data object Decimal : ConvertKey(".")

    data object Value1 : ConvertKey("V1")

    data object Value2 : ConvertKey("V2")

    data object Unit1 : ConvertKey("U1")

    data object Unit2 : ConvertKey("U2")

    data object Clear : ConvertKey("C")

    data object Backspace : ConvertKey(R.drawable.ic_backspace_24px.toString())

    data object Left : ConvertKey(R.drawable.ic_left_24px.toString())

    data object Right : ConvertKey(R.drawable.ic_right_24px.toString())
}
