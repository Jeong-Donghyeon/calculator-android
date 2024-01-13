package dev.donghyeon.calculator.calculate

import dev.donghyeon.calculator.calculate.fakedata.percent.Percent1DecimalExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.Percent1IntegerExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.Percent2DecimalExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.Percent2IntegerExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.Percent3DecimalExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.Percent3IntegerExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.Percent4DecimalExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.Percent4IntegerExpected
import dev.donghyeon.calculator.calculate.fakedata.percent.PercentDecimalInput
import dev.donghyeon.calculator.calculate.fakedata.percent.PercentIntegerInput
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class PercentTest {
    private val percent1 = Percent1UseCase(FormatNumber())
    private val percent2 = Percent2UseCase(FormatNumber())
    private val percent3 = Percent3UseCase(FormatNumber())
    private val percent4 = Percent4UseCase(FormatNumber())

    @Test
    fun test_Percent1() {
        assertArrayEquals(
            Percent1IntegerExpected,
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent1IntegerExpected,
                percent = { v1, v2 -> percent1(v1, v2) },
            ),
        )
        assertArrayEquals(
            Percent1DecimalExpected,
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent1DecimalExpected,
                percent = { v1, v2 -> percent1(v1, v2) },
            ),
        )
    }

    @Test
    fun test_Percent2() {
        assertArrayEquals(
            Percent2IntegerExpected,
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent2IntegerExpected,
                percent = { v1, v2 -> percent2(v1, v2) },
            ),
        )
        assertArrayEquals(
            Percent2DecimalExpected,
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent2DecimalExpected,
                percent = { v1, v2 -> percent2(v1, v2) },
            ),
        )
    }

    @Test
    fun test_Percent3() {
        assertArrayEquals(
            Percent3IntegerExpected,
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent3IntegerExpected,
                percent = { v1, v2 -> percent3(v1, v2) },
            ),
        )
        assertArrayEquals(
            Percent3DecimalExpected,
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent3DecimalExpected,
                percent = { v1, v2 -> percent3(v1, v2) },
            ),
        )
    }

    @Test
    fun test_Percent4() {
        assertArrayEquals(
            Percent4IntegerExpected,
            calculate(
                inputArr = PercentIntegerInput,
                expectedArr = Percent4IntegerExpected,
                percent = { v1, v2 -> percent4(v1, v2) },
            ),
        )
        assertArrayEquals(
            Percent4DecimalExpected,
            calculate(
                inputArr = PercentDecimalInput,
                expectedArr = Percent4DecimalExpected,
                percent = { v1, v2 -> percent4(v1, v2) },
            ),
        )
    }

    private fun calculate(
        inputArr: Array<String>,
        expectedArr: Array<String>,
        percent: (String, String) -> String,
    ): Array<String> {
        val count = inputArr.count()
        val actualsArr =
            inputArr.mapIndexed { i, v1 ->
                inputArr.mapIndexed { j, v2 ->
                    percent(v1, v2).also { actuals ->
                        val expected = expectedArr[count * i + j]
                        val result = if (expected == actuals) "PASS" else "FAIL"
                        println("$v1 | $v2 | $expected | $actuals | $result")
                    }
                }
            }.flatten().toTypedArray()
        return actualsArr
    }
}
