package dev.donghyeon.calculator.percent

import androidx.compose.ui.text.input.TextFieldValue
import dev.donghyeon.calculator.calculate.PercentCalculateType

data class PercentState(
    val type: PercentCalculateType = PercentCalculateType.TYPE1,
    val calculate1: Calculate = Calculate(),
    val calculate2: Calculate = Calculate(),
    val calculate3: Calculate = Calculate(),
    val calculate4: Calculate = Calculate(),
) {
    data class Calculate(
        val select: PercentValueSelect = PercentValueSelect.VALUE1,
        val value1: TextFieldValue = TextFieldValue(),
        val value2: TextFieldValue = TextFieldValue(),
        val result: String = "?",
    )
}

enum class PercentValueSelect(val value: String) {
    VALUE1(PercentKey.VALUE1.value),
    VALUE2(PercentKey.VALUE2.value),
}

enum class PercentKey(val value: String) {
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
    COPY("CP"),
    DECIMAL("."),
    BACK("⌫"),
    CLEAR("C"),
    LEFT("<-"),
    RIGHT("->"),
    VALUE1("V1"),
    VALUE2("V2"),
}
