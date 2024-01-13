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
    )

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
    }
