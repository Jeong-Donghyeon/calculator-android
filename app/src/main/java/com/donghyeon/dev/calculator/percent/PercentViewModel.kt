package com.donghyeon.dev.calculator.percent

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.PercentType
import com.donghyeon.dev.calculator.calculate.PercentUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
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

    fun inputKey(key: PercentKey)
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
                val type = PercentType.entries[repository.loadPersentType()]
                _state.value = PercentState(type = type)
            }
        }

        override fun inputType(index: Int) {
            viewModelScope.launch {
                repository.savePersentType(index)
            }
            _state.value =
                state.value.let { state ->
                    PercentType.entries.find { it.ordinal == index }?.let {
                        viewModelScope.launch {
                            _sideEffect.emit(SideEffect.Focus(state.getCalculate(it).select.value))
                        }
                        state.copy(type = it)
                    } ?: state
                }
        }

        override fun inputKey(key: PercentKey) {
            _state.value =
                state.value.let { state ->
                    state.copy(
                        calculateList =
                            state.calculateList.mapIndexed { index, calculate ->
                                if (index == state.type?.ordinal) {
                                    input(key, calculate)
                                } else {
                                    calculate
                                }
                            },
                    )
                }
        }

        private fun input(
            key: PercentKey,
            calculate: PercentState.Calculate,
        ): PercentState.Calculate {
            val type = state.value.type ?: return calculate
            val value = calculate.getValue()
            val newValueList: (TextFieldValue) -> List<TextFieldValue> = {
                calculate.valueList.mapIndexed { index, value ->
                    if (index == calculate.select.index) it else value
                }
            }
            val newCalculate =
                when (key) {
                    is PercentKey.Copy -> calculate
                    is PercentKey.Clear ->
                        calculate.copy(
                            valueList = newValueList(TextFieldValue()),
                            result = "?",
                        )
                    is PercentKey.Left -> {
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
                    is PercentKey.Right -> {
                        val index = value.selection.start + 1
                        calculate.copy(
                            valueList =
                                newValueList(
                                    value.copy(selection = TextRange(index)),
                                ),
                        )
                    }
                    is PercentKey.Value1, PercentKey.Value2 ->
                        PercentValue.entries.find { it.value == key.value }?.let { select ->
                            calculate.copy(select = select)
                        } ?: calculate
                    else -> {
                        val decimalCheck = value.text.any { it == '.' }
                        val digitsLimitCheck =
                            value.text
                                .replace(".", "")
                                .count() >= 10
                        if (key is PercentKey.Decimal && decimalCheck) {
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
                                        is PercentKey.Backspace -> {
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
                                    if (key is PercentKey.Backspace) {
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
                    percentUseCase(
                        type = type,
                        value1 = newCalculate.valueList[PercentValue.VALUE1.index].text,
                        value2 = newCalculate.valueList[PercentValue.VALUE2.index].text,
                    ),
            )
        }
    }
