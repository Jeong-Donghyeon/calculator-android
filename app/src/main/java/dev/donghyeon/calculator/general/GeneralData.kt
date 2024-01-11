package dev.donghyeon.calculator.general

import androidx.compose.ui.text.input.TextFieldValue

data class GeneralData(
    val select: GeneralSelect = GeneralSelect.CALCULATE1,
    val calculate1: Calculate = Calculate(),
    val calculate2: Calculate = Calculate(),
    val calculate3: Calculate = Calculate(),
    val calculate4: Calculate = Calculate(),
) {
    data class Calculate(
        val v: TextFieldValue = TextFieldValue(),
        val result: String = "?",
    )
}

enum class GeneralSelect(val value: String) {
    CALCULATE1("계산 1"),
    CALCULATE2("계산 2"),
    CALCULATE3("계산 3"),
    CALCULATE4("계산 4"),
}

enum class GeneralKeyPad(val value: String) {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    ZERO("0"),
    ZERO_ZERO("00"),
    DECIMAL("."),
    BACK("⌫"),
    CLEAR("C"),
    LEFT("<-"),
    RIGHT("->"),
    RESULT("="),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("x"),
    DIVIDE("÷"),
    SQUARE("^"),
    OPEN("("),
    CLOSE(")"),
}
