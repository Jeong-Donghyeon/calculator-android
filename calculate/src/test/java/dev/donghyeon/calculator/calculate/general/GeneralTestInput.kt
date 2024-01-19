package dev.donghyeon.calculator.calculate.general

import dev.donghyeon.calculator.calculate.GenralOperator

private val GeneralOperatorList =
    listOf(
        GenralOperator.PLUS.value,
        GenralOperator.MINUS.value,
        GenralOperator.MULTIPLY.value,
        GenralOperator.DIVIDE.value,
    ).let {
        it.map { v1 ->
            val l1 = it - v1
            l1.map { v2 ->
                val l2 = l1 - v2
                l2.map { v3 ->
                    val l3 = l2 - v3
                    l3.map { v4 ->
                        listOf(v1, v2, v3, v4)
                    }
                }.flatten()
            }.flatten()
        }.flatten()
    }

private val GeneralIntegerList =
    listOf(
        "123",
        "023",
        "120",
        "020",
        "0013",
    ).let {
        it.map { v1 ->
            val l1 = it - v1
            l1.map { v2 ->
                val l2 = l1 - v2
                l2.map { v3 ->
                    val l3 = l2 - v3
                    l3.map { v4 ->
                        val l4 = l3 - v4
                        l4.map { v5 ->
                            listOf(v1, v2, v3, v4, v5)
                        }
                    }.flatten()
                }.flatten()
            }.flatten()
        }.flatten()
    }

val GeneralIntegerInput =
    GeneralOperatorList.map { o ->
        GeneralIntegerList.map { v ->
            v[0] + o[0] + v[1] + o[1] + v[2] + o[2] + v[3] + o[3] + v[4]
        }
    }.flatten()

private val GeneralDecimalList =
    listOf(
        "123.0013",
        "023.123",
        "120.023",
        "020.120",
        "0013.020",
    ).let {
        it.map { v1 ->
            val l1 = it - v1
            l1.map { v2 ->
                val l2 = l1 - v2
                l2.map { v3 ->
                    val l3 = l2 - v3
                    l3.map { v4 ->
                        val l4 = l3 - v4
                        l4.map { v5 ->
                            listOf(v1, v2, v3, v4, v5)
                        }
                    }.flatten()
                }.flatten()
            }.flatten()
        }.flatten()
    }

val GeneralDecimalInput =
    GeneralOperatorList.map { o ->
        GeneralDecimalList.map { v ->
            v[0] + o[0] + v[1] + o[1] + v[2] + o[2] + v[3] + o[3] + v[4]
        }
    }.flatten()

val GeneralExpressionInput =
    arrayOf(
        "(3+5)×2",
        "7×4-9",
        "10÷2+3.5",
        "6-(2×4)",
        "2×(8+1.5)",
        "15.2÷2-1",
        "3×(6-4)+7",
        "9-5÷2",
        "(4+2)×3.5-1",
        "2.5×3+6÷2",
    )
