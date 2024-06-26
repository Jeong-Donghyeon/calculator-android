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
    data object History : GeneralKey(R.drawable.ic_history_24px.toString())

    data object Clear : GeneralKey("C")

    data object Left : GeneralKey(R.drawable.ic_left_24px.toString())

    data object Right : GeneralKey(R.drawable.ic_right_24px.toString())

    data object Backspace : GeneralKey(R.drawable.ic_backspace_24px.toString())

    data object Copy : GeneralKey(R.drawable.ic_copy_24px.toString())

    data class Paste(val result: String) : GeneralKey(R.drawable.ic_paste_24px.toString())

    data object Open : GeneralKey(GenralOperator.OPEN.value)

    data object Close : GeneralKey(GenralOperator.CLOSE.value)

    data object Divide : GeneralKey(GenralOperator.DIVIDE.value)

    data object Multiply : GeneralKey(GenralOperator.MULTIPLY.value)

    data object Minus : GeneralKey(GenralOperator.MINUS.value)

    data object Plus : GeneralKey(GenralOperator.PLUS.value)

    data object Equal : GeneralKey("=")

    data object Zero : GeneralKey("0")

    data object Decimal : GeneralKey(".")

    data object One : GeneralKey("1")

    data object Two : GeneralKey("2")

    data object Three : GeneralKey("3")

    data object Four : GeneralKey("4")

    data object Five : GeneralKey("5")

    data object Six : GeneralKey("6")

    data object Seven : GeneralKey("7")

    data object Eight : GeneralKey("8")

    data object Nine : GeneralKey("9")
}
