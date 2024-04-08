package com.donghyeon.dev.calculator.ratio

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.calculate.RatioUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface RatioAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)

    fun inputKey(key: RatioKey)
}

@HiltViewModel
class RatioViewModel
    @Inject
    constructor(
        private val ratioUseCase: RatioUseCase,
    ) : BaseViewModel(), RatioAction {
        private val _state = MutableStateFlow(RatioState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        override val sideEffect = _sideEffect.asSharedFlow()

        override fun inputType(index: Int) {
            _state.value =
                state.value.let { state ->
                    RatioType.entries.find { it.index == index }?.let {
                        viewModelScope.launch {
                            _sideEffect.emit(SideEffect.Focus(RatioValue.VALUE1.value))
                        }
                        state.copy(
                            type = it,
                            calculateList =
                                state.calculateList.map { calculate ->
                                    calculate.copy(select = RatioValue.VALUE1)
                                },
                        )
                    } ?: state
                }
        }

        override fun inputKey(key: RatioKey) {
            _state.value =
                state.value.let { state ->
                    state.copy(
                        calculateList =
                            state.calculateList.mapIndexed { index, calculate ->
                                if (index == state.type.index) {
                                    input(key, calculate)
                                } else {
                                    calculate
                                }
                            },
                    )
                }
        }

        private fun input(
            key: RatioKey,
            calculate: RatioState.Calculate,
        ): RatioState.Calculate {
            val value = calculate.getValue()
            val newValueList: (TextFieldValue) -> List<TextFieldValue> = {
                calculate.valueList.mapIndexed { index, value ->
                    if (index == calculate.select.index) it else value
                }
            }
            val newCalculate =
                when (key) {
                    is RatioKey.Clear ->
                        calculate.copy(
                            valueList = newValueList(TextFieldValue()),
                            result = "?",
                        )
                    is RatioKey.Left -> {
                        val index =
                            value.selection.start.let {
                                if (it == 0) 0 else it - 1
                            }
                        calculate.copy(
                            valueList =
                                newValueList(
                                    value.copy(selection = TextRange(index)),
                                ),
                        )
                    }
                    is RatioKey.Right -> {
                        val index = value.selection.start + 1
                        calculate.copy(
                            valueList =
                                newValueList(
                                    value.copy(selection = TextRange(index)),
                                ),
                        )
                    }
                    is RatioKey.Value1, RatioKey.Value2, RatioKey.Value3 ->
                        RatioValue.entries.find { it.value == key.value }?.let { select ->
                            calculate.copy(select = select)
                        } ?: calculate
                    else -> {
                        val decimalCheck = value.text.any { it == '.' }
                        val digitsLimitCheck =
                            value.text
                                .replace(".", "")
                                .count() >= 10
                        if (key is RatioKey.Decimal && decimalCheck) {
                            viewModelScope.launch {
                                _sideEffect.emit(SideEffect.Toast(R.string.error_decimal))
                            }
                            calculate
                        } else if (key.value.isDigitsOnly() && digitsLimitCheck) {
                            viewModelScope.launch {
                                _sideEffect.emit(SideEffect.Toast(R.string.error_digit))
                            }
                            calculate
                        } else {
                            val text =
                                StringBuilder(value.text).let {
                                    val index = value.selection.start
                                    when (key) {
                                        is RatioKey.Backspace -> {
                                            if (index == 0) {
                                                it.toString()
                                            } else {
                                                it.delete(index - 1, index).toString()
                                            }
                                        }
                                        else -> it.insert(index, key.value).toString()
                                    }
                                }
                            val index =
                                value.selection.start.let {
                                    if (key is RatioKey.Backspace) {
                                        if (it == 0) 0 else it - 1
                                    } else {
                                        it + key.value.count()
                                    }
                                }
                            calculate.copy(
                                valueList =
                                    newValueList(
                                        value.copy(
                                            text = text,
                                            selection = TextRange(index),
                                        ),
                                    ),
                            )
                        }
                    }
                }
            return newCalculate.copy(
                result =
                    ratioUseCase(
                        type = state.value.type,
                        valueList = newCalculate.valueList.map { it.text },
                    ),
            )
        }
    }
