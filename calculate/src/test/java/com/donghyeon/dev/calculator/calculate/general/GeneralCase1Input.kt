package com.donghyeon.dev.calculator.calculate.general

import com.donghyeon.dev.calculator.calculate.GenralOperator

val GeneralCase1Input =
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
    }.map { o ->
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
        }.map { v ->
            v[0] + o[0] + v[1] + o[1] + v[2] + o[2] + v[3] + o[3] + v[4]
        }
    }.flatten()
