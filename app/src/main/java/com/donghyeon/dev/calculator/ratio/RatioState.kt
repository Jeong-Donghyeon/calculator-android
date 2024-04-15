package com.donghyeon.dev.calculator.ratio

import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.state.Calculate

data class RatioState(
    val type: RatioType? = null,
    val v1Focus: Boolean = false,
    val v2Focus: Boolean = false,
    val v3Focus: Boolean = false,
    val calculateList: List<Calculate> =
        List(
            size = RatioType.entries.count(),
            init = {
                Calculate(
                    valueList =
                        List(
                            size = if (it == 0) 3 else 2,
                            init = { TextFieldValue() },
                        ),
                )
            },
        ),
) {
    fun getCalculate(): Calculate = getCalculate(type)

    private fun getCalculate(type: RatioType?): Calculate = calculateList[type?.ordinal ?: 0]

    fun getValueIndex(): Int =
        if (!v1Focus && v2Focus && !v3Focus) {
            1
        } else if (!v1Focus && !v2Focus && v3Focus) {
            2
        } else {
            0
        }

    fun getValue(): TextFieldValue = getCalculate().valueList[getValueIndex()]
}
