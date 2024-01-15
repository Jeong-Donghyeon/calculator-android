package dev.donghyeon.calculator.calculate

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Stack
import javax.inject.Inject

class GeneralUseCase
    @Inject
    constructor(
        private val format: ResultFormatGeneral,
        private val postfix: GeneralPostfix,
    ) {
        operator fun invoke(input: String): String {
            val postfix = postfix(input)
            if (postfix.isEmpty()) return "?"
            val stack = Stack<String>()
            postfix.forEach { v ->
                if (GenralOperator.entries.any { it.value == v }) {
                    if (stack.empty()) return "?"
                    val v1 = stack.pop().toBigDecimalOrNull() ?: return "?"
                    if (stack.empty()) return "?"
                    val v2 = stack.pop().toBigDecimalOrNull() ?: return "?"
                    val calculate =
                        when (v) {
                            GenralOperator.MULTIPLY.value -> v2.multiply(v1)
                            GenralOperator.DIVIDE.value -> {
                                if (v1 != BigDecimal.ZERO && v2 != BigDecimal.ZERO) {
                                    v2.divide(v1, 15, RoundingMode.UP)
                                } else {
                                    BigDecimal.ZERO
                                }
                            }
                            GenralOperator.PLUS.value -> v2.plus(v1)
                            GenralOperator.MINUS.value -> v2.minus(v1)
                            else -> return "?"
                        }
                    stack.push(calculate.toString())
                } else {
                    stack.push(v)
                }
            }
            return format(stack.pop())
        }
    }
