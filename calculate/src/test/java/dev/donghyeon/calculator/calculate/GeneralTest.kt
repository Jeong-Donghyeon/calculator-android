package dev.donghyeon.calculator.calculate

import org.junit.Test

class GeneralTest {
    @Test
    fun test() {
        // + - × ÷
        val formatPostfix = FormatPostfix()
        val e = "12+34×56^2"
        val a = formatPostfix(e)
        println("e: $e")
        println("a: $a")
    }
}
