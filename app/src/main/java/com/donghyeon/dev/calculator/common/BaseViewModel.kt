package com.donghyeon.dev.calculator.common

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.state.Calculate
import com.donghyeon.dev.calculator.view.Keyboard
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseViewModel : ViewModel() {
    val sideEffect = MutableSharedFlow<SideEffect>()

    fun newCalculate(
        key: Keyboard,
        calculate: Calculate,
        valueIndex: Int,
        value: TextFieldValue,
        error: (Int) -> Unit,
    ): Calculate {
        val newValue: (TextFieldValue) -> List<TextFieldValue> = {
            calculate.valueList.mapIndexed { index, value ->
                if (index == valueIndex) {
                    it
                } else {
                    value
                }
            }
        }
        return when (key) {
            is Keyboard.Clear ->
                calculate.copy(
                    valueList = newValue(TextFieldValue()),
                    result = "?",
                )
            is Keyboard.Left -> {
                val index =
                    value.selection.start.let {
                        if (it == 0) 0 else it - 1
                    }
                val valueList = newValue(value.copy(selection = TextRange(index)))
                calculate.copy(valueList = valueList)
            }
            is Keyboard.Right -> {
                val index = value.selection.start + 1
                val valueList = newValue(value.copy(selection = TextRange(index)))
                calculate.copy(valueList = valueList)
            }
            is Keyboard.Backspace -> {
                val text =
                    StringBuilder(value.text).let {
                        val index = value.selection.start
                        if (index == 0) {
                            it.toString()
                        } else {
                            it.delete(index - 1, index).toString()
                        }
                    }
                val index =
                    value.selection.start.let {
                        if (it == 0) 0 else it - 1
                    }
                val valueList =
                    newValue(
                        value.copy(
                            text = text,
                            selection = TextRange(index),
                        ),
                    )
                calculate.copy(valueList = valueList)
            }
            is Keyboard.Copy, Keyboard.Enter -> calculate
            is Keyboard.Paste -> {
                val result =
                    if (key.result.count() > 10) {
                        key.result.substring(0, 10).let {
                            if (it == ".") {
                                it.dropLast(1)
                            } else {
                                it
                            }
                        }
                    } else {
                        key.result
                    }
                result.toBigDecimalOrNull()?.let {
                    val text = it.toString()
                    val valueList =
                        newValue(
                            value.copy(
                                text = text,
                                selection = TextRange(text.length),
                            ),
                        )
                    calculate.copy(valueList = valueList)
                } ?: calculate
            }
            is Keyboard.Decimal -> {
                if (value.text.any { it == '.' }) {
                    error(R.string.error_decimal)
                    calculate
                } else {
                    val text =
                        StringBuilder(value.text).let {
                            val index = value.selection.start
                            it.insert(index, key.value).toString()
                        }
                    val index =
                        value.selection.start.let {
                            it + key.value.count()
                        }
                    val valueList =
                        newValue(
                            value.copy(
                                text = text,
                                selection = TextRange(index),
                            ),
                        )
                    calculate.copy(valueList = valueList)
                }
            }
            is Keyboard.ZeroZero, Keyboard.Zero,
            Keyboard.One, Keyboard.Two, Keyboard.Three,
            Keyboard.Four, Keyboard.Five, Keyboard.Six,
            Keyboard.Seven, Keyboard.Eight, Keyboard.Nine,
            -> {
                val valueCount = value.text.replace(".", "").count()
                val digitsLimitCheck =
                    if (key == Keyboard.ZeroZero) {
                        valueCount >= 9
                    } else {
                        valueCount >= 10
                    }
                if (digitsLimitCheck) {
                    error(R.string.error_digit)
                    calculate
                } else {
                    val text =
                        StringBuilder(value.text).let {
                            val index = value.selection.start
                            it.insert(index, key.value).toString()
                        }
                    val index =
                        value.selection.start.let {
                            it + key.value.count()
                        }
                    val valueList =
                        newValue(
                            value.copy(
                                text = text,
                                selection = TextRange(index),
                            ),
                        )
                    calculate.copy(valueList = valueList)
                }
            }
        }
    }
}
