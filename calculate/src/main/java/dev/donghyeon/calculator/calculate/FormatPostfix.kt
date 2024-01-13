package dev.donghyeon.calculator.calculate

import java.util.Stack
import javax.inject.Inject

class FormatPostfix
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
            println("checkInfix")
            val result = mutableListOf<String>()
            val oper = Stack<String>()
            infixList.forEach {
                if (it == "(") {
                    oper.push(it)
                } else if (it == ")") {
                    var open = true
                    while (open) {
                        val pop =
                            if (oper.empty()) {
                                break
                            } else {
                                oper.pop()
                            }
                        if (pop == "(") {
                            open = false
                        } else {
                            result.add(pop)
                        }
                    }
                } else if (it == "×" || it == "÷") {
                    if (oper.empty()) {
                        oper.push(it)
                    } else {
                        var md = true
                        while (md) {
                            val last =
                                if (oper.empty()) {
                                    break
                                } else {
                                    oper.last()
                                }
                            if (last == "×" || last == "÷") {
                                result.add(oper.pop())
                            } else {
                                md = false
                            }
                        }
                        oper.push(it)
                    }
                } else if (it == "+" || it == "-") {
                    if (oper.empty()) {
                        oper.push(it)
                    } else {
                        var pm = true
                        while (pm) {
                            val last =
                                if (oper.empty()) {
                                    break
                                } else {
                                    oper.last()
                                }
                            if (last == "(") {
                                oper.push(oper.pop())
                                pm = false
                            } else {
                                result.add(oper.pop())
                            }
                        }
                        oper.push(it)
                    }
                } else {
                    result.add(it)
                }
            }
            result.addAll(oper.reversed())
            return result
        }

        private fun checkInfix(infixList: List<String>): Boolean {
            println("infixList: $infixList")
            val checkValue =
                infixList.all { v ->
                    v.toBigDecimalOrNull() != null || Operator.entries.any { v == it.value }
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
