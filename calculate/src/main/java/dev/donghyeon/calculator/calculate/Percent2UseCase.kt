package dev.donghyeon.calculator.calculate

import java.math.RoundingMode
import javax.inject.Inject

class Percent2UseCase
    @Inject
    constructor(
        private val formatNumber: FormatNumber,
    ) {
        operator fun invoke(
            v1: String,
            v2: String,
        ): String {
            val value1 = v1.toBigDecimalOrNull() ?: return "?"
            val value2 = v2.toBigDecimalOrNull() ?: return "?"
            val result =
                value2.divide(value1, 10, RoundingMode.DOWN)
                    .multiply("100".toBigDecimal()).toString()
            return formatNumber(result) + "%"
        }
    }
