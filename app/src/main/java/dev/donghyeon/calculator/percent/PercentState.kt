package dev.donghyeon.calculator.percent

import androidx.compose.ui.text.input.TextFieldValue

data class PercentState(
    val select: PercentSelect = PercentSelect.CALCULATE1,
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

enum class PercentSelect(val value: String) {
    CALCULATE1("비율값"),
    CALCULATE2("일부값"),
    CALCULATE3("증감값"),
    CALCULATE4("증감율"),
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
    ZERO_ZERO("00"),
    DECIMAL("."),
    BACK("⌫"),
    CLEAR("C"),
    LEFT("<-"),
    RIGHT("->"),
    VALUE1("V1"),
    VALUE2("V2"),
}
