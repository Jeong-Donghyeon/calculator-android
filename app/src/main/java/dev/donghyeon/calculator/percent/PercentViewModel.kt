package dev.donghyeon.calculator.percent

import dev.donghyeon.calculator.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.RoundingMode
import javax.inject.Inject

interface PercentAction {
    fun input(s: String)
}

class PercentViewModel
    @Inject
    constructor() : BaseViewModel(), PercentAction {
        private val _percentState = MutableStateFlow(PercentData())
        val percentState: StateFlow<PercentData> = _percentState

        override fun input(s: String) {
            val state = percentState.value
            val (value1, value2, flag) =
                when (s) {
                    "<" ->
                        if (state.valueFlag) {
                            Triple(state.value1, state.value2.dropLast(1), true)
                        } else {
                            Triple(state.value1.dropLast(1), state.value2, false)
                        }
                    "value1" -> Triple(state.value1, state.value2, false)
                    "value2" -> Triple(state.value1, state.value2, true)
                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" ->
                        if (state.valueFlag) {
                            Triple(state.value1, state.value2 + s, true)
                        } else {
                            Triple(state.value1 + s, state.value2, false)
                        }
                    else -> Triple("", "", false)
                }
            val v1 = value1.toBigDecimalOrNull()
            val v2 = value2.toBigDecimalOrNull()
            val result =
                if (v1 != null && v2 != null) {
                    val p = v2.divide("100".toBigDecimal(), 2, RoundingMode.DOWN)
                    v1.multiply(p).toString()
                } else {
                    null
                }
            _percentState.value =
                state.copy(
                    value1 = value1,
                    value2 = value2,
                    valueFlag = flag,
                    result = result ?: "",
                )
        }
    }
