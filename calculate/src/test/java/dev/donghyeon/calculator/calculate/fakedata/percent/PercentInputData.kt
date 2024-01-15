package dev.donghyeon.calculator.calculate.fakedata.percent

val PercentIntegerInput =
    arrayOf(
        "123",
        "023",
        "120",
        "020",
        "0023",
        "1200",
        "00200",
    ).let {
        it.map { v1 ->
            it.map { v2 ->
                v1 to v2
            }
        }.flatten().toTypedArray()
    }

val PercentDecimalInput =
    listOf(
        "123",
        "023",
        "120",
        "020",
    ).let {
        it.map { i ->
            it.map { d ->
                "$i.$d"
            }
        }.flatten().toTypedArray()
    }.let {
        it.map { v1 ->
            it.map { v2 ->
                v1 to v2
            }
        }.flatten().toTypedArray()
    }
