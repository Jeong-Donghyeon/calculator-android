package com.donghyeon.dev.calculator.convert

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.ConvertType
import com.donghyeon.dev.calculator.calculate.ConvertUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ConvertAction {
    fun inputType(index: Int)

    fun inputKey(key: ConvertKey)
}

@HiltViewModel
class ConvertViewModel
    @Inject
    constructor(
        private val convertUseCase: ConvertUseCase,
    ) : BaseViewModel(), ConvertAction {
        private val _state = MutableStateFlow(ConvertState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        override fun inputType(index: Int) {
            _state.value =
                state.value.let { state ->
                    ConvertType.entries.find { it.index == index }?.let {
                        viewModelScope.launch {
                            _sideEffect.emit(SideEffect.Focus(state.getCalculate(it).select.value))
                        }
                        state.copy(type = it)
                    } ?: state
                }
        }

        override fun inputKey(key: ConvertKey) {
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
            key: ConvertKey,
            calculate: ConvertState.Calculate,
        ): ConvertState.Calculate {
            val value = calculate.getValue()
            val newValueList: (TextFieldValue) -> List<TextFieldValue> = {
                calculate.valueList.mapIndexed { index, value ->
                    if (index == calculate.select.index) it else value
                }
            }
            val newCalculate =
                when (key) {
                    is ConvertKey.Clear ->
                        calculate.copy(
                            valueList = newValueList(TextFieldValue()),
                            result = "?",
                        )
                    is ConvertKey.Left -> {
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
                    is ConvertKey.Right -> {
                        val index = value.selection.start + 1
                        calculate.copy(
                            valueList =
                                newValueList(
                                    value.copy(selection = TextRange(index)),
                                ),
                        )
                    }
                    is ConvertKey.Value1, ConvertKey.Value2 ->
                        ConvertValue.entries.find { it.value == key.value }?.let { select ->
                            calculate.copy(select = select)
                        } ?: calculate
                    else -> {
                        val decimalCheck = value.text.any { it == '.' }
                        val digitsLimitCheck =
                            value.text
                                .replace(".", "")
                                .count() >= 10
                        if (key is ConvertKey.Decimal && decimalCheck) {
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
                                        is ConvertKey.Backspace -> {
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
                                    if (key is ConvertKey.Backspace) {
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
                    convertUseCase(
                        type = state.value.type,
                        value1 = newCalculate.valueList[ConvertValue.VALUE1.index].text,
                        value2 = newCalculate.valueList[ConvertValue.VALUE2.index].text,
                    ),
            )
        }
    }
