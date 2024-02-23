package com.donghyeon.dev.calculator.calculate

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Stack
import javax.inject.Inject

enum class GenralOperator(val value: String) {
    MULTIPLY("×"),
    DIVIDE("÷"),
    PLUS("+"),
    MINUS("-"),
    OPEN("("),
    CLOSE(")"),
}

class GeneralUseCase
    @Inject
    constructor() {
        operator fun invoke(input: String): String {
            try {
                val postfix = infixToPostfix(input)
                if (postfix.isEmpty()) return ""
                if (postfix.count() == 1) return ""
                val stack = Stack<String>()
                postfix.forEach { v ->
                    if (GenralOperator.entries.any { it.value == v }) {
                        if (stack.empty()) return ""
                        val v1 = stack.pop().toBigDecimalOrNull() ?: return ""
                        if (stack.empty()) return ""
                        val v2 = stack.pop().toBigDecimalOrNull() ?: return ""
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
                                else -> return ""
                            }
                        stack.push(calculate.toString())
                    } else {
                        stack.push(v)
                    }
                }
                return format(stack.pop())
            } catch (e: Exception) {
                return ""
            }
        }

        private fun infixToPostfix(infix: String): List<String> {
            val infixList = mutableListOf<String>()
            var row = ""
            var minus = false
            infix.forEachIndexed { i, v ->
                val value = v.toString()
                if (v.isDigit()) {
                    row +=
                        if (minus) {
                            minus = false
                            "-$value"
                        } else {
                            value
                        }
                } else if (v == '.') {
                    row += value
                } else {
                    if (row != "") {
                        infixList.add(row)
                        row = ""
                    }
                    if (infixList.isEmpty()) {
                        infixList.add(value)
                    } else {
                        when (infixList.last()) {
                            GenralOperator.MULTIPLY.value,
                            GenralOperator.DIVIDE.value,
                            GenralOperator.OPEN.value,
                            -> {
                                when (value) {
                                    GenralOperator.PLUS.value -> minus = false
                                    GenralOperator.MINUS.value -> minus = true
                                    else -> infixList.add(value)
                                }
                            }
                            GenralOperator.PLUS.value,
                            GenralOperator.MINUS.value,
                            GenralOperator.CLOSE.value,
                            -> infixList.add(value)
                            else -> {
                                if (value == GenralOperator.OPEN.value) {
                                    infixList.add(GenralOperator.MULTIPLY.value)
                                }
                                infixList.add(value)
                            }
                        }
                        if (value == GenralOperator.CLOSE.value) {
                            if (i < infix.count() - 1) {
                                val multiply =
                                    GenralOperator.entries.all {
                                        it.value != infix[i + 1].toString()
                                    }
                                if (multiply) {
                                    infixList.add(GenralOperator.MULTIPLY.value)
                                }
                            }
                        }
                    }
                }
                if (i == infix.lastIndex && row != "") {
                    infixList.add(row)
                }
            }
            if (!checkInfix(infixList)) return emptyList()
            val result = mutableListOf<String>()
            val oper = Stack<String>()
            infixList.forEach {
                when (it) {
                    GenralOperator.OPEN.value -> oper.push(it)
                    GenralOperator.CLOSE.value -> {
                        while (true) {
                            if (oper.empty()) {
                                break
                            } else {
                                val pop = oper.pop()
                                if (pop == GenralOperator.OPEN.value) {
                                    break
                                } else {
                                    result.add(pop)
                                }
                            }
                        }
                    }
                    GenralOperator.MULTIPLY.value, GenralOperator.DIVIDE.value -> {
                        if (oper.empty()) {
                            oper.push(it)
                        } else {
                            while (true) {
                                if (oper.empty()) {
                                    oper.push(it)
                                    break
                                }
                                when (oper.peek()) {
                                    GenralOperator.OPEN.value -> {
                                        oper.push(it)
                                        break
                                    }
                                    GenralOperator.CLOSE.value -> {}
                                    GenralOperator.MULTIPLY.value, GenralOperator.DIVIDE.value -> {
                                        result.add(oper.pop())
                                    }
                                    GenralOperator.PLUS.value, GenralOperator.MINUS.value -> {
                                        oper.push(it)
                                        break
                                    }
                                }
                            }
                        }
                    }
                    GenralOperator.PLUS.value, GenralOperator.MINUS.value -> {
                        if (oper.empty()) {
                            oper.push(it)
                        } else {
                            while (true) {
                                if (oper.empty()) {
                                    oper.push(it)
                                    break
                                }
                                when (oper.peek()) {
                                    GenralOperator.OPEN.value -> {
                                        oper.push(it)
                                        break
                                    }
                                    GenralOperator.CLOSE.value -> {}
                                    else -> result.add(oper.pop())
                                }
                            }
                        }
                    }
                    else -> result.add(it)
                }
            }
            result.addAll(oper.reversed())
            return result
        }

        private fun checkInfix(infixList: List<String>): Boolean {
            val checkValue =
                infixList.all { v ->
                    v.toBigDecimalOrNull() != null || GenralOperator.entries.any { v == it.value }
                }
            var checkBracket: Boolean? = null
            var bracketCount = 0
            infixList.forEach {
                if (it == "(") bracketCount += 1
                if (it == ")") bracketCount -= 1
                if (bracketCount < 0) checkBracket = false
            }
            checkBracket = checkBracket ?: (bracketCount == 0)
            return checkValue && checkBracket == true
        }

        private fun format(result: String): String {
            val decimalFormat =
                DecimalFormat("#,###.######").apply {
                    roundingMode = RoundingMode.DOWN
                }
            var formatResult =
                result.toBigDecimalOrNull()?.let {
                    decimalFormat.format(it)
                } ?: result
            formatResult.split(".").let {
                formatResult =
                    if (it.count() > 1) {
                        var decimal = it[1]
                        if (decimal.count() > 4) {
                            if (decimal.reversed().substring(0, 3) == "999") {
                                decimal = decimal.toIntOrNull()?.plus(1)?.toString() ?: decimal
                            }
                            decimal = decimal.substring(0, 4)
                        }
                        val zero =
                            decimal.all { d ->
                                d == '0'
                            }
                        if (zero) {
                            it.first()
                        } else {
                            while (true) {
                                if (decimal.last() == '0') {
                                    decimal = decimal.dropLast(1)
                                } else {
                                    break
                                }
                            }
                            it.first() + "." + decimal
                        }
                    } else {
                        formatResult
                    }
                if (formatResult == "-0") formatResult = "0"
            }
            return formatResult
        }
    }
