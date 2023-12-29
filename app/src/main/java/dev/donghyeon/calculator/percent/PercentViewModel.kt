package dev.donghyeon.calculator.percent

import dev.donghyeon.calculator.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

interface PercentAction {
    fun inputCalculateSelect()

    fun inputValueSelect(num: Int)

    fun inputClear()

    fun inputBack()

    fun inputNumber(s: String)
}

class PercentViewModel
    @Inject
    constructor() : BaseViewModel(), PercentAction {
        private val _percentState = MutableStateFlow(PercentData())
        val percentState: StateFlow<PercentData> = _percentState

        override fun inputCalculateSelect() {
            val state = percentState.value
            _percentState.value =
                state.copy(
                    calculateSelect =
                        if (state.calculateSelect < 4) {
                            state.calculateSelect + 1
                        } else {
                            1
                        },
                )
        }

        override fun inputValueSelect(num: Int) {
            val state = percentState.value
            _percentState.value =
                when (state.calculateSelect) {
                    1 ->
                        when (num) {
                            1 -> state.copy(calculate1 = state.calculate1.copy(valueSelect = 1))
                            2 -> state.copy(calculate1 = state.calculate1.copy(valueSelect = 2))
                            else -> state
                        }
                    2 ->
                        when (num) {
                            1 -> state.copy(calculate2 = state.calculate2.copy(valueSelect = 1))
                            2 -> state.copy(calculate2 = state.calculate2.copy(valueSelect = 2))
                            else -> state
                        }
                    3 ->
                        when (num) {
                            1 -> state.copy(calculate3 = state.calculate3.copy(valueSelect = 1))
                            2 -> state.copy(calculate3 = state.calculate3.copy(valueSelect = 2))
                            else -> state
                        }
                    4 ->
                        when (num) {
                            1 -> state.copy(calculate4 = state.calculate4.copy(valueSelect = 1))
                            2 -> state.copy(calculate4 = state.calculate4.copy(valueSelect = 2))
                            else -> state
                        }
                    else -> state
                }
        }

        override fun inputClear() {
            val state = percentState.value
            _percentState.value =
                when (state.calculateSelect) {
                    1 -> state.copy(calculate1 = PercentData.Calculate(mode = 1))
                    2 -> state.copy(calculate2 = PercentData.Calculate(mode = 2))
                    3 -> state.copy(calculate3 = PercentData.Calculate(mode = 3))
                    4 -> state.copy(calculate4 = PercentData.Calculate(mode = 4))
                    else -> state
                }
        }

        override fun inputBack() {
            val state = percentState.value
            val newState =
                when (state.calculateSelect) {
                    1 ->
                        state.copy(
                            calculate1 =
                                when (state.calculate1.valueSelect) {
                                    1 ->
                                        state.calculate1.copy(
                                            value1 =
                                                state.calculate1.value1.dropLast(1).let {
                                                    if (it == "") "?" else it
                                                },
                                        )
                                    2 ->
                                        state.calculate1.copy(
                                            value2 =
                                                state.calculate1.value2.dropLast(1).let {
                                                    if (it == "") "?" else it
                                                },
                                        )
                                    else -> state.calculate1
                                },
                        )
                    else -> state
                }
            calculate1(newState)
        }

        override fun inputNumber(s: String) {
            val state = percentState.value
            when (state.calculateSelect) {
                1 ->
                    calculate1(
                        state.copy(
                            calculate1 =
                                when (state.calculate1.valueSelect) {
                                    1 -> state.calculate1.copy(value1 = state.calculate1.value1.replace("?", "") + s)
                                    2 -> state.calculate1.copy(value2 = state.calculate1.value2.replace("?", "") + s)
                                    else -> state.calculate1
                                },
                        ),
                    )
                2 ->
                    calculate2(
                        state.copy(
                            calculate2 =
                                when (state.calculate2.valueSelect) {
                                    1 -> state.calculate2.copy(value1 = state.calculate2.value1.replace("?", "") + s)
                                    2 -> state.calculate2.copy(value2 = state.calculate2.value2.replace("?", "") + s)
                                    else -> state.calculate2
                                },
                        ),
                    )
                3 ->
                    calculate3(
                        state.copy(
                            calculate3 =
                                when (state.calculate3.valueSelect) {
                                    1 -> state.calculate3.copy(value1 = state.calculate3.value1.replace("?", "") + s)
                                    2 -> state.calculate3.copy(value2 = state.calculate3.value2.replace("?", "") + s)
                                    else -> state.calculate3
                                },
                        ),
                    )
                4 ->
                    calculate4(
                        state.copy(
                            calculate4 =
                                when (state.calculate4.valueSelect) {
                                    1 -> state.calculate4.copy(value1 = state.calculate4.value1.replace("?", "") + s)
                                    2 -> state.calculate4.copy(value2 = state.calculate4.value2.replace("?", "") + s)
                                    else -> state.calculate4
                                },
                        ),
                    )
                else -> {}
            }
        }

        private fun calculate1(state: PercentData) {
            val v1 = state.calculate1.value1.toBigDecimalOrNull()
            val v2 = state.calculate1.value2.toBigDecimalOrNull()
            val result =
                if (v1 != null && v2 != null) {
                    val p = v2.divide("100".toBigDecimal(), 2, RoundingMode.DOWN)
                    v1.multiply(p).toString()
                } else {
                    null
                }
            _percentState.value =
                if (result == null) {
                    state.copy(calculate1 = state.calculate1.copy(result = "?"))
                } else {
                    state.copy(calculate1 = state.calculate1.copy(result = result.toScaleCommaString(scale = 2)))
                }
        }

        private fun calculate2(state: PercentData) {}

        private fun calculate3(state: PercentData) {}

        private fun calculate4(state: PercentData) {}
    }

fun String.toCommaString(): String =
    this.toBigIntegerOrNull()?.let { i ->
        this.split(".").let {
            if (it.count() > 1) {
                DecimalFormat("#,###").format(i) + "." + it[1]
            } else {
                DecimalFormat("#,###").format(i)
            }
        }
    } ?: this

fun String.toScaleCommaString(scale: Int): String {
    val minus = this.contains("-")
    val value = this.replace("-", "")
    val result =
        value.split(".").let {
            if (it.count() > 1) {
                var rv =
                    if (scale < it[1].count()) {
                        it[1].substring(0, scale)
                    } else {
                        it[1]
                    }
                val lvV = it[0].toCommaString()
                if (lvV.count() > 13) {
                    lvV
                } else {
                    if (rv == "" || rv.all { v -> v == '0' }) {
                        lvV
                    } else {
                        while (rv.last() == '0') {
                            rv = rv.dropLast(1)
                        }
                        val v = "$lvV.$rv"
                        if (v.count() > 15) {
                            v.substring(0, 15)
                        } else {
                            v
                        }
                    }
                }
            } else {
                value.toCommaString()
            }
        }
    return if (minus) "-$result" else result
}
