package com.donghyeon.dev.calculator.percent

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.calculate.PercentType
import com.donghyeon.dev.calculator.state.Calculate

data class PercentState(
    val type: PercentType? = null,
    val v1Focus: Boolean = false,
    val v2Focus: Boolean = false,
    val calculateList: List<Calculate> =
        List(
            size = PercentType.entries.count(),
            init = {
                Calculate(
                    valueList =
                        List(
                            size = 2,
                            init = { TextFieldValue() },
                        ),
                )
            },
        ),
) {
    fun getCalculate(): Calculate = getCalculate(type)

    private fun getCalculate(type: PercentType?): Calculate = calculateList[type?.ordinal ?: 0]

    fun getValueIndex(): Int =
        if (!v1Focus && v2Focus) {
            1
        } else {
            0
        }

    fun getValue(): TextFieldValue = getCalculate().valueList[getValueIndex()]
}
