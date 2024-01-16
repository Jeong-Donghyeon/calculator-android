package dev.donghyeon.calculator.calculate

import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class ResultFormatGeneral
    @Inject
    constructor() {
        operator fun invoke(number: String): String =
            number.toBigDecimalOrNull()?.let {
                val result =
                    DecimalFormat("#,###.######").apply {
                        roundingMode = RoundingMode.DOWN
                    }.format(it)
                result.split(".").let { n ->
                    if (n.count() > 1) {
                        var d = n[1]
                        if (d.count() > 4) {
                            if (d.reversed().substring(0, 3) == "999") {
                                d = d.toIntOrNull()?.plus(1)?.toString() ?: d
                            }
                            d = d.substring(0, 4)
                        }
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
