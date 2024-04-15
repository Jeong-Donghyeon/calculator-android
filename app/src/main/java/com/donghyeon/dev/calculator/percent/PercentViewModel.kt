package com.donghyeon.dev.calculator.percent

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.substring
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.PercentType
import com.donghyeon.dev.calculator.calculate.PercentUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
import com.donghyeon.dev.calculator.view.Keyboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PercentAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)

    fun inputV1Focus()

    fun inputV2Focus()

    fun inputKey(key: Keyboard)
}

@HiltViewModel
class PercentViewModel
    @Inject
    constructor(
        private val percentUseCase: PercentUseCase,
        private val repository: Repository,
    ) : BaseViewModel(), PercentAction {
        private val _state = MutableStateFlow(PercentState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        override val sideEffect = _sideEffect.asSharedFlow()

        init {
            viewModelScope.launch {
                val type = PercentType.entries[repository.loadPercentType()]
                _state.value = PercentState(type = type)
            }
        }

        override fun inputType(index: Int) {
            viewModelScope.launch {
                repository.savePercentType(index)
            }
            PercentType.entries.find { it.ordinal == index }?.let {
                _state.value = state.value.copy(type = it)
            }
        }

        override fun inputV1Focus() {
            _state.value =
                state.value.copy(
                    v1Focus = true,
                    v2Focus = false,
                )
        }

        override fun inputV2Focus() {
            _state.value =
                state.value.copy(
                    v1Focus = false,
                    v2Focus = true,
                )
        }

        override fun inputKey(key: Keyboard) {
            val state = state.value
            _state.value =
                state.copy(
                    calculateList =
                        state.calculateList.mapIndexed { index, calculate ->
                            if (index == state.type?.ordinal) {
                                input(key)
                            } else {
                                calculate
                            }
                        },
                )
        }

        private fun input(key: Keyboard): PercentState.Calculate {
            val state = state.value
            val calculate = state.getCalculate()
            val type = state.type ?: return calculate
            val value = state.getValue()
            val newValue: (TextFieldValue) -> List<TextFieldValue> = {
                calculate.valueList.mapIndexed { index, value ->
                    if (index == state.getValueIndex()) {
                        it
                    } else {
                        value
                    }
                }
            }
            val newCalculate =
                when (key) {
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
                            viewModelScope.launch {
                                _sideEffect.emit(SideEffect.Toast(R.string.error_decimal))
                            }
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
                            viewModelScope.launch {
                                _sideEffect.emit(SideEffect.Toast(R.string.error_digit))
                            }
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
            return newCalculate.copy(
                result =
                    percentUseCase(
                        type = type,
                        value1 = newCalculate.valueList[0].text,
                        value2 = newCalculate.valueList[1].text,
                    ),
            )
        }
    }
