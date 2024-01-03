package dev.donghyeon.calculator.usecase

import java.text.DecimalFormat
import javax.inject.Inject

class NumberFormatString
    @Inject
    constructor() {
        operator fun invoke(number: String): String =
            number.toBigDecimalOrNull()?.let {
                val result = DecimalFormat("#,###.##").format(it)
                result.split(".").let { n ->
                    if (n.count() > 1) {
                        var d = n[1]
                        while (d.last() == '0') {
                            d = d.dropLast(1)
                        }
                        n.first() + "." + d
                    } else {
                        result
                    }
                }
            } ?: number
    }