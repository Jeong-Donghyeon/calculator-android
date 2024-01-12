package dev.donghyeon.calculator.calculate

import dev.donghyeon.calculator.calculate.fakedata.percent.eDecimalPercentCalculate1
import dev.donghyeon.calculator.calculate.fakedata.percent.eDecimalPercentCalculate2
import dev.donghyeon.calculator.calculate.fakedata.percent.eDecimalPercentCalculate3
import dev.donghyeon.calculator.calculate.fakedata.percent.eDecimalPercentCalculate4
import dev.donghyeon.calculator.calculate.fakedata.percent.eIntegerPercentCalculate1
import dev.donghyeon.calculator.calculate.fakedata.percent.eIntegerPercentCalculate2
import dev.donghyeon.calculator.calculate.fakedata.percent.eIntegerPercentCalculate3
import dev.donghyeon.calculator.calculate.fakedata.percent.eIntegerPercentCalculate4
import dev.donghyeon.calculator.calculate.fakedata.percent.vDecimalArr
import dev.donghyeon.calculator.calculate.fakedata.percent.vIntArr
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class PercentTest {
    private val useCase1 = Percent1UseCase(FormatNumber())
    private val useCase2 = Percent2UseCase(FormatNumber())
    private val useCase3 = Percent3UseCase(FormatNumber())
    private val useCase4 = Percent4UseCase(FormatNumber())

    @Test
    fun test_Integer_PercentCalculate1UseCase() {
        val count = vIntArr.count()
        val aArr =
            vIntArr.mapIndexed { i, v1 ->
                vIntArr.mapIndexed { j, v2 ->
                    useCase1(v1, v2).also { a ->
                        val e = eIntegerPercentCalculate1[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate1UseCase I | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eIntegerPercentCalculate1, aArr)
    }

    @Test
    fun test_Decimal_PercentCalculate1UseCase() {
        val count = vDecimalArr.count()
        val aArr =
            vDecimalArr.mapIndexed { i, v1 ->
                vDecimalArr.mapIndexed { j, v2 ->
                    useCase1(v1, v2).also { a ->
                        val e = eDecimalPercentCalculate1[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate1UseCase D | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eDecimalPercentCalculate1, aArr)
    }

    @Test
    fun test_Integer_PercentCalculate2UseCase() {
        val count = vIntArr.count()
        val aArr =
            vIntArr.mapIndexed { i, v1 ->
                vIntArr.mapIndexed { j, v2 ->
                    useCase2(v1, v2).also { a ->
                        val e = eIntegerPercentCalculate2[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate2UseCase I | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eIntegerPercentCalculate2, aArr)
    }

    @Test
    fun test_Decimal_PercentCalculate2UseCase() {
        val count = vDecimalArr.count()
        val aArr =
            vDecimalArr.mapIndexed { i, v1 ->
                vDecimalArr.mapIndexed { j, v2 ->
                    useCase2(v1, v2).also { a ->
                        val e = eDecimalPercentCalculate2[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate2UseCase D | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eDecimalPercentCalculate2, aArr)
    }

    @Test
    fun test_Integer_PercentCalculate3UseCase() {
        val count = vIntArr.count()
        val aArr =
            vIntArr.mapIndexed { i, v1 ->
                vIntArr.mapIndexed { j, v2 ->
                    useCase3(v1, v2).also { a ->
                        val e = eIntegerPercentCalculate3[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate3UseCase I | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eIntegerPercentCalculate3, aArr)
    }

    @Test
    fun test_Decimal_PercentCalculate3UseCase() {
        val count = vDecimalArr.count()
        val aArr =
            vDecimalArr.mapIndexed { i, v1 ->
                vDecimalArr.mapIndexed { j, v2 ->
                    useCase3(v1, v2).also { a ->
                        val e = eDecimalPercentCalculate3[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate3UseCase D | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eDecimalPercentCalculate3, aArr)
    }

    @Test
    fun test_Integer_PercentCalculate4UseCase() {
        val count = vIntArr.count()
        val aArr =
            vIntArr.mapIndexed { i, v1 ->
                vIntArr.mapIndexed { j, v2 ->
                    useCase4(v1, v2).also { a ->
                        val e = eIntegerPercentCalculate4[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate4UseCase I | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eIntegerPercentCalculate4, aArr)
    }

    @Test
    fun test_Decimal_PercentCalculate4UseCase() {
        val count = vDecimalArr.count()
        val aArr =
            vDecimalArr.mapIndexed { i, v1 ->
                vDecimalArr.mapIndexed { j, v2 ->
                    useCase4(v1, v2).also { a ->
                        val e = eDecimalPercentCalculate4[count * i + j]
                        val r = if (e == a) "PASS" else "FAIL"
                        println("PercentCalculate4UseCase D | v1: $v1 | v2: $v2 | e: $e | a: $a | $r")
                    }
                }
            }.flatten().toTypedArray()
        assertArrayEquals(eDecimalPercentCalculate4, aArr)
    }
}
