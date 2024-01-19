package dev.donghyeon.calculator.calculate

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
                                    if (v1 > BigDecimal.ZERO && v2 > BigDecimal.ZERO) {
                                        println("v1: $v1 | v2: $v2")
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
            infix.forEachIndexed { i, v ->
                if (v.isDigit() || v == '.') {
                    row += v.toString()
                } else {
                    if (row != "") {
                        infixList.add(row)
                        row = ""
                    }
                    infixList.add(v.toString())
                }
                if (i == infix.lastIndex) {
                    infixList.add(row)
                }
            }
            if (!checkInfix(infixList)) return emptyList()
            val result = mutableListOf<String>()
            val oper = Stack<String>()
            infixList.forEach {
                when (it) {
                    GenralOperator.OPEN.value, GenralOperator.CLOSE.value -> {}
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
                                    GenralOperator.OPEN.value, GenralOperator.CLOSE.value -> {}
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
                                } else {
                                    result.add(oper.pop())
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
                        var d = it[1]
                        if (d.count() > 4) {
                            if (d.reversed().substring(0, 3) == "999") {
                                d = d.toIntOrNull()?.plus(1)?.toString() ?: d
                            }
                            d = d.substring(0, 4)
                        }
                        while (d.last() == '0') {
                            d = d.dropLast(1)
                        }
                        it.first() + "." + d
                    } else {
                        formatResult
                    }
            }
            return formatResult
        }
    }
