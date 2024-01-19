package dev.donghyeon.calculator.calculate.general

import dev.donghyeon.calculator.calculate.GeneralUseCase
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class GeneralTest {
    private val useCase = GeneralUseCase()

    @Test
    fun test_GeneralInteger() {
        val actuals =
            GeneralIntegerInput.mapIndexed { index, input ->
                val expected = GeneralIntegerExpected[index]
                val actuals = useCase(input)
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
                val actuals = useCase(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralDecimalExpected, actuals)
    }

    @Test
    fun test_GeneralExpression() {
        val actuals =
            GeneralExpressionInput.mapIndexed { index, input ->
                val expected = GeneralExpressionExpected[index]
                val actuals = useCase(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralExpressionExpected, actuals)
    }
}
