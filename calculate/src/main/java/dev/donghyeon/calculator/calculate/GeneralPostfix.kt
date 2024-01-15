package dev.donghyeon.calculator.calculate

import java.util.Stack
import javax.inject.Inject

class GeneralPostfix
    @Inject
    constructor() {
        operator fun invoke(infix: String): List<String> {
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
                                    GenralOperator.OPEN.value, GenralOperator.CLOSE.value-> {}
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
    }
