package dev.donghyeon.calculator.calculate

import dev.donghyeon.calculator.calculate.fakedata.general.GeneralDecimalInput
import dev.donghyeon.calculator.calculate.fakedata.general.GeneralExpectedDecimal
import dev.donghyeon.calculator.calculate.fakedata.general.GeneralExpectedInteger
import dev.donghyeon.calculator.calculate.fakedata.general.GeneralIntegerInput
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class GeneralTest {
    private val general =
        GeneralUseCase(
            format = ResultFormatGeneral(),
            postfix = GeneralPostfix(),
        )

    @Test
    fun test_GeneralInteger() {
        val actuals =
            GeneralIntegerInput.mapIndexed { index, input ->
                val expected = GeneralExpectedInteger[index]
                val actuals = general(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralExpectedInteger, actuals)
    }

    @Test
    fun test_GeneralDecimal() {
        val actuals =
            GeneralDecimalInput.mapIndexed { index, input ->
                val expected = GeneralExpectedDecimal[index]
                val actuals = general(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralExpectedDecimal, actuals)
    }
}
