package dev.donghyeon.calculator.usecase.percent

import dev.donghyeon.calculator.usecase.NumberFormatString
import java.math.RoundingMode
import javax.inject.Inject

class PercentCalculate1UseCase
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
            val value = value2.divide("100".toBigDecimal(), 2, RoundingMode.DOWN)
            val result = value1.multiply(value).toString()
            return numberFormatString(result)
        }
    }
