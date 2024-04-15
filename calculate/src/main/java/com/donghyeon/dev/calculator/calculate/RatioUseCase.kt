package com.donghyeon.dev.calculator.calculate

import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.pow

enum class RatioType {
    RATIO,
    SIMPLIFY,
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
            var v1 = valueList[0].toBigDecimalOrNull() ?: return defaultValue
            var v2 = valueList[1].toBigDecimalOrNull() ?: return defaultValue
            val m = 10.0.pow(max(v1.scale(), v2.scale()))
            if (m > 0) {
                v1 = v1.multiply(m.toBigDecimal())
                v2 = v2.multiply(m.toBigDecimal())
            }
            val gcd = gcd(v1.toInt(), v2.toInt())
            val result1 =
                format(
                    v1.divide(gcd.toBigDecimal(), 10, RoundingMode.DOWN)
                        .divide(m.toBigDecimal()).toString(),
                )
            val result2 =
                format(
                    v2.divide(gcd.toBigDecimal(), 10, RoundingMode.DOWN)
                        .divide(m.toBigDecimal()).toString(),
                )
            return "$result1:$result2"
        }

        private fun gcd(
            a: Int,
            b: Int,
        ): Int = if (b != 0) gcd(b, a % b) else a

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
