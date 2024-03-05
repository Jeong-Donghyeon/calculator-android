package com.donghyeon.dev.calculator.calculate

import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

enum class RatioType(val index: Int) {
    RATIO(0),
    SIMPLIFY(1),
}

class RatioUseCase
    @Inject
    constructor() {
        private val defaultValue: String = "?"

        operator fun invoke(
            type: RatioType,
            valueList: List<String>,
        ): String =
            try {
                when (type) {
                    RatioType.RATIO -> ratio(valueList = valueList)
                    RatioType.SIMPLIFY -> simplify(valueList = valueList)
                }
            } catch (e: Exception) {
                defaultValue
            }

        private fun ratio(valueList: List<String>): String {
            val v1 = valueList[0].toBigDecimalOrNull() ?: return defaultValue
            val v2 = valueList[1].toBigDecimalOrNull() ?: return defaultValue
            val v3 = valueList[2].toBigDecimalOrNull() ?: return defaultValue
            val result = v3.multiply(v2).divide(v1, 10, RoundingMode.DOWN).toString()
            return format(result)
        }

        private fun simplify(valueList: List<String>): String {
            val v1 = valueList[0].toBigDecimalOrNull() ?: return defaultValue
            val v2 = valueList[1].toBigDecimalOrNull() ?: return defaultValue
            return defaultValue
        }

        private fun format(result: String): String {
            val decimalFormat =
                DecimalFormat("#,###.##").apply {
                    roundingMode = RoundingMode.DOWN
                }
            return result.toBigDecimalOrNull()?.let {
                decimalFormat.format(it)
            } ?: result
        }
    }
