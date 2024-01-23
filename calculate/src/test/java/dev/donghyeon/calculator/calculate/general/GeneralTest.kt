package dev.donghyeon.calculator.calculate.general

import dev.donghyeon.calculator.calculate.GeneralUseCase
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class GeneralTest {
    private val useCase = GeneralUseCase()

    @Test
    fun test_GeneralCase1() {
        val actuals =
            GeneralCase1Input.mapIndexed { index, input ->
                val expected = GeneralCase1Expected[index]
                val actuals = useCase(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralCase1Expected, actuals)
    }

    @Test
    fun test_GeneralCase2() {
        val actuals =
            GeneralCase2Input.mapIndexed { index, input ->
                val expected = GeneralCase2Expected[index]
                val actuals = useCase(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralCase2Expected, actuals)
    }

    @Test
    fun test_GeneralCase3() {
        val actuals =
            GeneralCase3Input.mapIndexed { index, input ->
                val expected = GeneralCase3Expected[index]
                val actuals = useCase(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralCase3Expected, actuals)
    }

    @Test
    fun test_GeneralCase4() {
        val actuals =
            GeneralCase4Input.mapIndexed { index, input ->
                val expected = GeneralCase4Expected[index]
                val actuals = useCase(input)
                val result = expected == actuals
                println("$input = $expected | $actuals | $result")
                actuals
            }.toTypedArray()
        assertArrayEquals(GeneralCase4Expected, actuals)
    }
}
