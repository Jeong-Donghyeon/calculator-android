package dev.donghyeon.calculator.calculate.percent

import dev.donghyeon.calculator.calculate.PercentCalculateType
import dev.donghyeon.calculator.calculate.PercentUseCase
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class PercentTest {
    private val useCase = PercentUseCase()

    @Test
    fun test_Percent1() {
        val case1Actuals =
            calculate(
                inputArr = PercentCase1Input,
                expectedArr = Percent1Case1Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE1, v1, v2)
                },
            )
        val case2Actuals =
            calculate(
                inputArr = PercentCase2Input,
                expectedArr = Percent1Case2Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE1, v1, v2)
                },
            )
        assertArrayEquals(Percent1Case1Expected, case1Actuals)
        assertArrayEquals(Percent1Case2Expected, case2Actuals)
    }

    @Test
    fun test_Percent2() {
        val case1Actuals =
            calculate(
                inputArr = PercentCase1Input,
                expectedArr = Percent2Case1Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE2, v1, v2)
                },
            )
        val case2Actuals =
            calculate(
                inputArr = PercentCase2Input,
                expectedArr = Percent2Case2Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE2, v1, v2)
                },
            )
        assertArrayEquals(Percent2Case1Expected, case1Actuals)
        assertArrayEquals(Percent2Case2Expected, case2Actuals)
    }

    @Test
    fun test_Percent3() {
        val case1Actuals =
            calculate(
                inputArr = PercentCase1Input,
                expectedArr = Percent3Case1Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE3, v1, v2)
                },
            )
        val case2Actuals =
            calculate(
                inputArr = PercentCase2Input,
                expectedArr = Percent3Case2Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE3, v1, v2)
                },
            )
        assertArrayEquals(Percent3Case1Expected, case1Actuals)
        assertArrayEquals(Percent3Case2Expected, case2Actuals)
    }

    @Test
    fun test_Percent4() {
        val case1Actuals =
            calculate(
                inputArr = PercentCase1Input,
                expectedArr = Percent4Case1Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE4, v1, v2)
                },
            )
        val case2Actuals =
            calculate(
                inputArr = PercentCase2Input,
                expectedArr = Percent4Case2Expected,
                percent = { v1, v2 ->
                    useCase(PercentCalculateType.TYPE4, v1, v2)
                },
            )
        assertArrayEquals(Percent4Case1Expected, case1Actuals)
        assertArrayEquals(Percent4Case2Expected, case2Actuals)
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
