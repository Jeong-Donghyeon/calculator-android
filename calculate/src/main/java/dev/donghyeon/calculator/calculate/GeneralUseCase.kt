package dev.donghyeon.calculator.calculate

import java.math.RoundingMode
import java.util.Stack
import javax.inject.Inject

class GeneralUseCase
    @Inject
    constructor(
        private val formatNumber: FormatNumber,
        private val formatPostfix: FormatPostfix,
    ) {
        operator fun invoke(input: String): String {
            val postfix = formatPostfix(input)
            if (postfix.isEmpty()) return "?"
            val stack = Stack<String>()
            postfix.forEach { v ->
                if (Operator.entries.any { it.value == v }) {
                    if (stack.empty()) return "?"
                    val v1 = stack.pop().toBigDecimalOrNull() ?: return "?"
                    if (stack.empty()) return "?"
                    val v2 = stack.pop().toBigDecimalOrNull() ?: return "?"
                    val calculate =
                        when (v) {
                            Operator.SQUARE.value -> return "?"
                            Operator.MULTIPLY.value -> v1.multiply(v2)
                            Operator.DIVIDE.value -> v1.divide(v2, 10, RoundingMode.DOWN)
                            Operator.PLUS.value -> v1.plus(v2)
                            Operator.MINUS.value -> v1.minus(v2)
                            else -> return "?"
                        }
                    stack.push(calculate.toString())
                } else {
                    stack.push(v)
                }
            }
            return formatNumber(stack.pop())
        }
    }
