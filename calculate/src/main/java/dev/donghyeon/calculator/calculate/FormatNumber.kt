package dev.donghyeon.calculator.calculate

import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class FormatNumber
    @Inject
    constructor() {
        operator fun invoke(
            number: String,
            scaleDown: Boolean = false,
        ): String =
            number.toBigDecimalOrNull()?.let {
                val format =
                    if (scaleDown) {
                        "#,###.##"
                    } else {
                        "#,###.####"
                    }
                val result =
                    DecimalFormat(format).apply {
                        roundingMode = RoundingMode.DOWN
                    }.format(it)
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
