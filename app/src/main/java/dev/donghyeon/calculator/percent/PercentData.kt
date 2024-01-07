package dev.donghyeon.calculator.percent

import androidx.compose.ui.text.input.TextFieldValue

data class PercentData(
    val select: PercentSelect = PercentSelect.CALCULATE1,
    val calculate1: Calculate = Calculate(),
    val calculate2: Calculate = Calculate(),
    val calculate3: Calculate = Calculate(),
    val calculate4: Calculate = Calculate(),
) {
    data class Calculate(
        val select: ValueSelect = ValueSelect.V1,
        val v1: TextFieldValue = TextFieldValue(),
        val v2: TextFieldValue = TextFieldValue(),
        val result: String = "?",
    )
}

enum class PercentSelect(val value: String) {
    CALCULATE1("비율값"),
    CALCULATE2("일부값"),
    CALCULATE3("증감값"),
    CALCULATE4("증감율"),
}

enum class ValueSelect { NONE, V1, V2 }

enum class NumberPadKey(val value: String) {
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
    ZERO3("000"),
    DECIMAL("."),
    BACK("back"),
    CLEAR("clear"),
    LEFT("left"),
    RIGHT("right"),
}
