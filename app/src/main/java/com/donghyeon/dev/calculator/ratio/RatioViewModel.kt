package com.donghyeon.dev.calculator.ratio

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.calculate.RatioUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface RatioAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)

    fun inputV1Focus()

    fun inputV2Focus()

    fun inputV3Focus()

    fun inputKey(key: RatioKey)
}

@HiltViewModel
class RatioViewModel @Inject constructor(
    private val ratioUseCase: RatioUseCase,
    private val repository: Repository,
) : BaseViewModel(), RatioAction {
    private val _state = MutableStateFlow(RatioState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val type = RatioType.entries[repository.loadRatioType()]
            _state.value = RatioState(type = type)
        }
    }

    override fun inputType(index: Int) {
        viewModelScope.launch {
            repository.saveRatioType(index)
        }
        RatioType.entries.find { it.ordinal == index }?.let {
            _state.value = state.value.copy(type = it)
        }
    }

    override fun inputV1Focus() {
        _state.value =
            state.value.copy(
                v1Focus = true,
                v2Focus = false,
                v3Focus = false,
            )
    }

    override fun inputV2Focus() {
        _state.value =
            state.value.copy(
                v1Focus = false,
                v2Focus = true,
                v3Focus = false,
            )
    }

    override fun inputV3Focus() {
        _state.value =
            state.value.copy(
                v1Focus = false,
                v2Focus = false,
                v3Focus = true,
            )
    }

    override fun inputKey(key: RatioKey) {
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

    private fun input(key: RatioKey): RatioState.Calculate {
        val state = state.value
        val calculate = state.getCalculate()
        val type = state.type ?: return calculate
        val newCalculate =
            newCalculate(
                key = key,
                calculate = calculate,
                valueIndex = state.getValueIndex(),
                value = state.getValue(),
                error = {
                    viewModelScope.launch {
                        sideEffect.emit(SideEffect.Toast(it))
                    }
                },
            )
        return newCalculate.copy(
            result =
                ratioUseCase(
                    type = type,
                    valueList = newCalculate.valueList.map { it.text },
                ),
        )
    }

    private fun newCalculate(
        key: RatioKey,
        calculate: RatioState.Calculate,
        valueIndex: Int,
        value: TextFieldValue,
        error: (Int) -> Unit,
    ): RatioState.Calculate {
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
            is RatioKey.Clear ->
                calculate.copy(
                    valueList = newValue(TextFieldValue()),
                    result = "?",
                )
            is RatioKey.Left -> {
                val index =
                    value.selection.start.let {
                        if (it == 0) 0 else it - 1
                    }
                val valueList = newValue(value.copy(selection = TextRange(index)))
                calculate.copy(valueList = valueList)
            }
            is RatioKey.Right -> {
                val index = value.selection.start + 1
                val valueList = newValue(value.copy(selection = TextRange(index)))
                calculate.copy(valueList = valueList)
            }
            is RatioKey.Backspace -> {
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
            is RatioKey.Copy, RatioKey.Enter -> calculate
            is RatioKey.Paste -> {
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
            is RatioKey.Decimal -> {
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
            is RatioKey.ZeroZero, RatioKey.Zero,
            RatioKey.One, RatioKey.Two, RatioKey.Three,
            RatioKey.Four, RatioKey.Five, RatioKey.Six,
            RatioKey.Seven, RatioKey.Eight, RatioKey.Nine,
            -> {
                val valueCount = value.text.replace(".", "").count()
                val digitsLimitCheck =
                    if (key == RatioKey.ZeroZero) {
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
