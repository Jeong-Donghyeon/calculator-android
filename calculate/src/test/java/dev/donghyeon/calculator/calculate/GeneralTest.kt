package dev.donghyeon.calculator.calculate

import dev.donghyeon.calculator.calculate.fakedata.general.GeneralExpected
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
    fun test_General() {
        val actuals = GeneralIntegerInput.mapIndexed { index, input ->
            val expected = GeneralExpected[index]
            val actuals = general(input)
            val result = expected == actuals
            println("$input = $expected | $actuals | $result")
            expected
        }.toTypedArray()
        assertArrayEquals(GeneralExpected, actuals)
    }
}
