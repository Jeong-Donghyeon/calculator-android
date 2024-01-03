package dev.donghyeon.calculator.usecase

import java.text.DecimalFormat
import javax.inject.Inject

class NumberFormatString
    @Inject
    constructor() {
        operator fun invoke(number: String): String =
            number.toBigDecimalOrNull()?.let {
                var result = DecimalFormat("#,###.##").format(it)
                while (result.last() == '0' || result.last() == '.') {
                    result = result.dropLast(1)
                }
                result
            } ?: number
    }
