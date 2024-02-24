package com.donghyeon.dev.calculator.general

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.GenralOperator

data class GeneralState(
    val history: Boolean = false,
    val historyList: List<Pair<String, String>> = emptyList(),
    val value: TextFieldValue = TextFieldValue(),
    val result: String = "",
)

sealed class GeneralKey(val value: String) {
    data object One : GeneralKey("1")

    data object Two : GeneralKey("2")

    data object Three : GeneralKey("3")

    data object Four : GeneralKey("4")

    data object Five : GeneralKey("5")

    data object Six : GeneralKey("6")

    data object Seven : GeneralKey("7")

    data object Eight : GeneralKey("8")

    data object Nine : GeneralKey("9")

    data object Zero : GeneralKey("0")

    data object ZeroZero : GeneralKey("00")

    data object Decimal : GeneralKey(".")

    data object Equal : GeneralKey("=")

    data object Clear : GeneralKey("C")

    data object Backspace : GeneralKey(R.drawable.ic_backspace_24px.toString())

    data object Left : GeneralKey(R.drawable.ic_left_24px.toString())

    data object Right : GeneralKey(R.drawable.ic_right_24px.toString())

    data object History : GeneralKey(R.drawable.ic_history_24px.toString())

    data object Multiply : GeneralKey(GenralOperator.MULTIPLY.value)

    data object Divide : GeneralKey(GenralOperator.DIVIDE.value)

    data object Plus : GeneralKey(GenralOperator.PLUS.value)

    data object Minus : GeneralKey(GenralOperator.MINUS.value)

    data object Open : GeneralKey(GenralOperator.OPEN.value)

    data object Close : GeneralKey(GenralOperator.CLOSE.value)
}
