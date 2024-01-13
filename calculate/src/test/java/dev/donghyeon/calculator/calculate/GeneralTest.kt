package dev.donghyeon.calculator.calculate

import org.junit.Test

class GeneralTest {
    private val useCase =
        GeneralUseCase(
            formatNumber = FormatNumber(),
            formatPostfix = FormatPostfix(),
        )

    @Test
    fun test() {
        // + - × ÷
        val e = "(9.5+5)×5"
        val a = useCase(e)
        println("e: $e")
        println("a: $a")
    }
}
