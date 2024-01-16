package dev.donghyeon.calculator.calculate.percent

import dev.donghyeon.calculator.calculate.PercentCalculateType
import dev.donghyeon.calculator.calculate.PercentUseCase
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class PercentTest {
    private val useCase = PercentUseCase()

    @Test
    fun test_Percent1() {
        val integerActuals =
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent1IntegerExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE1, v1, v2)
                },
            )
        val decimalActuals =
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent1DecimalExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE1, v1, v2)
                },
            )
        assertArrayEquals(Percent1IntegerExpected, integerActuals)
        assertArrayEquals(Percent1DecimalExpected, decimalActuals)
    }

    @Test
    fun test_Percent2() {
        val integerActuals =
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent2IntegerExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE2, v1, v2)
                },
            )
        val decimalActuals =
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent2DecimalExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE2, v1, v2)
                },
            )
        assertArrayEquals(Percent2IntegerExpected, integerActuals)
        assertArrayEquals(Percent2DecimalExpected, decimalActuals)
    }

    @Test
    fun test_Percent3() {
        val integerActuals =
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent3IntegerExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE3, v1, v2)
                },
            )
        val decimalActuals =
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent3DecimalExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE3, v1, v2)
                },
            )
        assertArrayEquals(Percent3IntegerExpected, integerActuals)
        assertArrayEquals(Percent3DecimalExpected, decimalActuals)
    }

    @Test
    fun test_Percent4() {
        val integerActuals =
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent4IntegerExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE4, v1, v2)
                },
            )
        val decimalActuals =
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent4DecimalExpected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE4, v1, v2)
                },
            )
        assertArrayEquals(Percent4IntegerExpected, integerActuals)
        assertArrayEquals(Percent4DecimalExpected, decimalActuals)
    }

    private fun calculate(
        inputArr: Array<Pair<String, String>>,
        expectedArr: Array<String>,
        percent: (String, String) -> String,
    ): Array<String> =
        inputArr.mapIndexed { index, input ->
            val v1 = input.first
            val v2 = input.second
            val expected = expectedArr[index]
            val actuals = percent(v1, v2)
            val result = expected == actuals
            println("$v1 % $v2 = $expected | $actuals | $result")
            actuals
        }.toTypedArray()
}
