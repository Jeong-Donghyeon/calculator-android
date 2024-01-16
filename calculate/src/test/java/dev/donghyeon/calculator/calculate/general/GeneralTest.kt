package dev.donghyeon.calculator.calculate.general

import dev.donghyeon.calculator.calculate.GeneralPostfix
import dev.donghyeon.calculator.calculate.GeneralUseCase
import dev.donghyeon.calculator.calculate.ResultFormatGeneral
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
                val expected = GeneralIntegerExpected[index]
                val actuals = general(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralIntegerExpected, actuals)
    }

    @Test
    fun test_GeneralDecimal() {
        val actuals =
            GeneralDecimalInput.mapIndexed { index, input ->
                val expected = GeneralDecimalExpected[index]
                val actuals = general(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralDecimalExpected, actuals)
    }
}
