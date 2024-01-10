package dev.donghyeon.calculator.general

data class GeneralData(
    val select: GeneralSelect = GeneralSelect.CALCULATE1,
)

enum class GeneralSelect(val value: String) {
    CALCULATE1("계산 1"),
    CALCULATE2("계산 2"),
    CALCULATE3("계산 3"),
    CALCULATE4("계산 4"),
}
