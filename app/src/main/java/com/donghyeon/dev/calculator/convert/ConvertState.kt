package com.donghyeon.dev.calculator.convert

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.ConvertType
import com.donghyeon.dev.calculator.calculate.unitLengthList

data class ConvertState(
    val type: ConvertType = ConvertType.LENGTH,
    val unit: String = unitLengthList[2],
    val unitValue: TextFieldValue = TextFieldValue(),
    val resultList: List<String> =
        listOf(
            unitLengthList[3],
            unitLengthList[4],
            unitLengthList[5],
        ),
    val resultValueList: List<String> = listOf("?", "?", "?"),
)

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

    data object Unit : ConvertKey("U")

    data object Result1 : ConvertKey("R1")

    data object Result2 : ConvertKey("R2")

    data object Result3 : ConvertKey("R3")

    data object Clear : ConvertKey("C")

    data object Backspace : ConvertKey(R.drawable.ic_backspace_24px.toString())

    data object Left : ConvertKey(R.drawable.ic_left_24px.toString())

    data object Right : ConvertKey(R.drawable.ic_right_24px.toString())
}
