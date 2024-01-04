package dev.donghyeon.calculator.domain

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class PercentCalculate3UseCase
    @Inject
    constructor(
        private val numberFormatString: NumberFormatString,
    ) {
        operator fun invoke(
            v1: String,
            v2: String,
        ): String {
            val value1 = v1.toBigDecimalOrNull() ?: return "?"
            val value2 = v2.toBigDecimalOrNull() ?: return "?"
            val value = value1.minus(value2)
            val updown = if (value <= BigDecimal.ZERO) "증가" else "감소"
            val result =
                value.abs().divide(value1, 10, RoundingMode.DOWN)
                    .multiply("100".toBigDecimal()).toString()
            return numberFormatString(result) + "% $updown"
        }
    }
