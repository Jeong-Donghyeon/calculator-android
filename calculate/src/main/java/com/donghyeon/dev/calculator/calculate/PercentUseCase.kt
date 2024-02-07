package com.donghyeon.dev.calculator.calculate

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

enum class PercentCalculateType(val value: String) {
    TYPE1("비율값"),
    TYPE2("일부값"),
    TYPE3("증감값"),
    TYPE4("증감율"),
}

enum class PercentUnit(val value: String) {
    PERCENT("%"),
    UP("증가"),
    DOWN("감소"),
}

class PercentUseCase
    @Inject
    constructor() {
        operator fun invoke(
            type: PercentCalculateType,
            value1: String,
            value2: String,
        ): String =
            try {
                when (type) {
                    PercentCalculateType.TYPE1 -> type1(value1, value2)
                    PercentCalculateType.TYPE2 -> type2(value1, value2)
                    PercentCalculateType.TYPE3 -> type3(value1, value2)
                    PercentCalculateType.TYPE4 -> type4(value1, value2)
                }
            } catch (e: Exception) {
                "?"
            }

        private fun type1(
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

        private fun type2(
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

        private fun type3(
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

        private fun type4(
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
