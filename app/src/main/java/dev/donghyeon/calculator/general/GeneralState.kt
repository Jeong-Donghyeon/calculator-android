package dev.donghyeon.calculator.general

import androidx.compose.ui.text.input.TextFieldValue
import dev.donghyeon.calculator.calculate.GenralOperator

data class GeneralState(
    val select: GeneralSelect = GeneralSelect.CALCULATE1,
    val calculate1: Calculate = Calculate(),
    val calculate2: Calculate = Calculate(),
    val calculate3: Calculate = Calculate(),
    val calculate4: Calculate = Calculate(),
) {
    data class Calculate(
        val value: TextFieldValue = TextFieldValue(),
        val result: String = "",
    )
}

enum class GeneralSelect(val value: String) {
    CALCULATE1("계산 1"),
    CALCULATE2("계산 2"),
    CALCULATE3("계산 3"),
    CALCULATE4("계산 4"),
}

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

    data object Decimal : GeneralKey(".")

    data object Back : GeneralKey("⌫")

    data object Clear : GeneralKey("C")

    data object Left : GeneralKey("<-")

    data object Right : GeneralKey("->")

    data object CopyResult : GeneralKey("CP")

    data object CopyExpress : GeneralKey("ECP")

    data class Past(val past: String = "") : GeneralKey("PS")

    data object Multiply : GeneralKey(GenralOperator.MULTIPLY.value)

    data object Divide : GeneralKey(GenralOperator.DIVIDE.value)

    data object Plus : GeneralKey(GenralOperator.PLUS.value)

    data object Minus : GeneralKey(GenralOperator.MINUS.value)

    data object Open : GeneralKey(GenralOperator.OPEN.value)

    data object Close : GeneralKey(GenralOperator.CLOSE.value)
}
