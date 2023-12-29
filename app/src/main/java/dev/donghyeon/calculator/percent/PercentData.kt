package dev.donghyeon.calculator.percent

data class PercentData(
    val calculateSelect: Int = 1,
    val calculate1: Calculate = Calculate(mode = 1),
    val calculate2: Calculate = Calculate(mode = 2),
    val calculate3: Calculate = Calculate(mode = 3),
    val calculate4: Calculate = Calculate(mode = 4),
) {
    data class Calculate(
        val mode: Int,
        val valueSelect: Int = 1,
        val value1: String = "?",
        val value2: String = "?",
        val result: String = "?",
    )
}
