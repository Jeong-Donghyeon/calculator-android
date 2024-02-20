package com.donghyeon.dev.calculator.calculate

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

enum class PercentType(val index: Int) {
    RATIO_VALUE(0),
    PERCENTAGE(1),
    RATE_OF_CHANGE(2),
    INCREMENT(3),
}

enum class PercentUnit(val value: String) {
    PERCENT("%"),
    UP("Up"),
    DOWN("Down"),
}

class PercentUseCase
    @Inject
    constructor() {
        operator fun invoke(
            type: PercentType,
            value1: String,
            value2: String,
        ): String =
            try {
                when (type) {
                    PercentType.RATIO_VALUE -> ratioValue(value1, value2)
                    PercentType.PERCENTAGE -> percentage(value1, value2)
                    PercentType.RATE_OF_CHANGE -> rateOfChange(value1, value2)
                    PercentType.INCREMENT -> increment(value1, value2)
                }
            } catch (e: Exception) {
                "?"
            }

        private fun ratioValue(
            value1: String,
            value2: String,
        ): String {
            val v1 = value1.toBigDecimalOrNull() ?: return "?"
            val v2 = value2.toBigDecimalOrNull() ?: return "?"
            val result =
                v1.multiply(v2)
                    .divide("100".toBigDecimal(), 10, RoundingMode.DOWN)
                    .toString()
            println(result)
            return format(result)
        }

        private fun percentage(
            value1: String,
            value2: String,
        ): String {
            val v1 = value1.toBigDecimalOrNull() ?: return "?"
            val v2 = value2.toBigDecimalOrNull() ?: return "?"
            val result =
                v2.divide(v1, 10, RoundingMode.DOWN)
                    .multiply("100".toBigDecimal()).toString()
            return format(result) + PercentUnit.PERCENT.value
        }

        private fun rateOfChange(
            value1: String,
            value2: String,
        ): String {
            val v1 = value1.toBigDecimalOrNull() ?: return "?"
            val v2 = value2.toBigDecimalOrNull() ?: return "?"
            val v = v1.minus(v2)
            val updown =
                if (v <= BigDecimal.ZERO) {
                    PercentUnit.UP.value
                } else {
                    PercentUnit.DOWN.value
                }
            val result =
                v.abs().divide(v1, 10, RoundingMode.DOWN)
                    .multiply("100".toBigDecimal()).toString()
            return format(result) + "${PercentUnit.PERCENT.value} $updown"
        }

        private fun increment(
            value1: String,
            value2: String,
        ): String {
            val v1 = value1.toBigDecimalOrNull() ?: return "?"
            val v2 = value2.toBigDecimalOrNull() ?: return "?"
            val v = v2.divide("100".toBigDecimal(), 10, RoundingMode.DOWN)
            val result = v1.multiply(v).plus(v1).toString()
            return format(result)
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
