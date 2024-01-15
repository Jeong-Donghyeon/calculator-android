package dev.donghyeon.calculator.calculate

import dev.donghyeon.calculator.calculate.fakedata.general.GeneralExpected
import dev.donghyeon.calculator.calculate.fakedata.general.GeneralIntegerInput
import org.junit.Test

class GeneralTest {
    private val general =
        GeneralUseCase(
            formatNumber = FormatNumber(),
            formatPostfix = FormatPostfix(),
        )

    @Test
    fun test() {
        GeneralIntegerInput.mapIndexed { index, input ->
            val expected = GeneralExpected[index]
            val actuals = general(input)
            val result = expected == actuals
            println("$input = $expected | $actuals | $result")
        }
    }
}
