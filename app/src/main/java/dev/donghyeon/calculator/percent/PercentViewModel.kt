package dev.donghyeon.calculator.percent

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.donghyeon.calculator.calculate.Percent1UseCase
import dev.donghyeon.calculator.calculate.Percent2UseCase
import dev.donghyeon.calculator.calculate.Percent3UseCase
import dev.donghyeon.calculator.calculate.Percent4UseCase
import dev.donghyeon.calculator.common.BaseViewModel
import dev.donghyeon.calculator.common.SideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PercentAction {
    fun inputPercentSelect(select: PercentSelect)

    fun inputPercentValueSelect(select: PercentValueSelect)

    fun inputKey(key: PercentKey)
}

@HiltViewModel
class PercentViewModel
    @Inject
    constructor(
        private val percent1UseCase: Percent1UseCase,
        private val percent2UseCase: Percent2UseCase,
        private val percent3UseCase: Percent3UseCase,
        private val percent4UseCase: Percent4UseCase,
    ) : BaseViewModel(), PercentAction {
        private val _state = MutableStateFlow(PercentState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        override fun inputPercentSelect(select: PercentSelect) {
            _state.value =
                state.value.let {
                    viewModelScope.launch {
                        val calculate =
                            when (select) {
                                PercentSelect.CALCULATE1 -> it.calculate1
                                PercentSelect.CALCULATE2 -> it.calculate2
                                PercentSelect.CALCULATE3 -> it.calculate3
                                PercentSelect.CALCULATE4 -> it.calculate4
                            }
                        _sideEffect.emit(SideEffect.Focus(calculate.select.value))
                    }
                    it.copy(select = select)
                }
        }

        override fun inputPercentValueSelect(select: PercentValueSelect) {
            _state.value =
                state.value.let {
                    when (it.select) {
                        PercentSelect.CALCULATE2 ->
                            it.copy(calculate2 = it.calculate2.copy(select = select))
                        PercentSelect.CALCULATE3 ->
                            it.copy(calculate3 = it.calculate3.copy(select = select))
                        PercentSelect.CALCULATE4 ->
                            it.copy(calculate4 = it.calculate4.copy(select = select))
                        else -> it.copy(calculate1 = it.calculate1.copy(select = select))
                    }
                }
        }

        override fun inputKey(key: PercentKey) {
            _state.value =
                state.value.let {
                    when (it.select) {
                        PercentSelect.CALCULATE2 ->
                            it.copy(calculate2 = input(key, it.calculate2))
                        PercentSelect.CALCULATE3 ->
                            it.copy(calculate3 = input(key, it.calculate3))
                        PercentSelect.CALCULATE4 ->
                            it.copy(calculate4 = input(key, it.calculate4))
                        else -> it.copy(calculate1 = input(key, it.calculate1))
                    }
                }
        }

        private fun input(
            key: PercentKey,
            calculate: PercentState.Calculate,
        ): PercentState.Calculate =
            when (key) {
                PercentKey.CLEAR ->
                    when (calculate.select) {
                        PercentValueSelect.VALUE1 ->
                            calculate.copy(value1 = TextFieldValue(), result = "?")
                        PercentValueSelect.VALUE2 ->
                            calculate.copy(value2 = TextFieldValue(), result = "?")
                    }
                PercentKey.LEFT ->
                    when (calculate.select) {
                        PercentValueSelect.VALUE1 -> {
                            val index =
                                calculate.value1.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            calculate.copy(value1 = calculate.value1.copy(selection = TextRange(index)))
                        }
                        PercentValueSelect.VALUE2 -> {
                            val index =
                                calculate.value2.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            calculate.copy(value2 = calculate.value2.copy(selection = TextRange(index)))
                        }
                    }
                PercentKey.RIGHT ->
                    when (calculate.select) {
                        PercentValueSelect.VALUE1 -> {
                            val index = calculate.value1.selection.start + 1
                            calculate.copy(value1 = calculate.value1.copy(selection = TextRange(index)))
                        }
                        PercentValueSelect.VALUE2 -> {
                            val index = calculate.value2.selection.start + 1
                            calculate.copy(value2 = calculate.value2.copy(selection = TextRange(index)))
                        }
                    }
                else -> {
                    val decimalMessage = "소수점은 하나만 입력하세요"
                    val digitsLimitMessage = "최대 10 자리수 입니다"
                    when (calculate.select) {
                        PercentValueSelect.VALUE1 -> {
                            val decimalCheck = checkDecimal(calculate.value1.text)
                            val digitsLimitCheck = checkDigitsLimit(calculate.value1.text, key)
                            if (key == PercentKey.DECIMAL && decimalCheck) {
                                viewModelScope.launch {
                                    _sideEffect.emit(SideEffect.Toast(decimalMessage))
                                }
                                calculate
                            } else if (key.value.isDigitsOnly() && digitsLimitCheck) {
                                viewModelScope.launch {
                                    _sideEffect.emit(SideEffect.Toast(digitsLimitMessage))
                                }
                                calculate
                            } else {
                                val inputTxt = inputKey(key, calculate.value1)
                                val index =
                                    calculate.value1.selection.start.let {
                                        if (key == PercentKey.BACK) {
                                            if (it == 0) 0 else it - 1
                                        } else {
                                            it + key.value.count()
                                        }
                                    }
                                val v1 =
                                    calculate.value1.copy(
                                        text = inputTxt,
                                        selection = TextRange(index),
                                    )
                                calculate.copy(value1 = v1)
                            }
                        }
                        PercentValueSelect.VALUE2 -> {
                            val decimalCheck = checkDecimal(calculate.value2.text)
                            val digitsLimitCheck = checkDigitsLimit(calculate.value2.text, key)
                            if (key == PercentKey.DECIMAL && decimalCheck) {
                                viewModelScope.launch {
                                    _sideEffect.emit(SideEffect.Toast(decimalMessage))
                                }
                                calculate
                            } else if (key.value.isDigitsOnly() && digitsLimitCheck) {
                                viewModelScope.launch {
                                    _sideEffect.emit(SideEffect.Toast(digitsLimitMessage))
                                }
                                calculate
                            } else {
                                val inputTxt = inputKey(key, calculate.value2)
                                val index =
                                    calculate.value2.selection.start.let {
                                        if (key == PercentKey.BACK) {
                                            if (it == 0) 0 else it - 1
                                        } else {
                                            it + key.value.count()
                                        }
                                    }
                                val v2 =
                                    calculate.value2.copy(
                                        text = inputTxt,
                                        selection = TextRange(index),
                                    )
                                calculate.copy(value2 = v2)
                            }
                        }
                    }.let {
                        val result =
                            when (state.value.select) {
                                PercentSelect.CALCULATE2 ->
                                    percent2UseCase(it.value1.text, it.value2.text)
                                PercentSelect.CALCULATE3 ->
                                    percent3UseCase(it.value1.text, it.value2.text)
                                PercentSelect.CALCULATE4 ->
                                    percent4UseCase(it.value1.text, it.value2.text)
                                else -> percent1UseCase(it.value1.text, it.value2.text)
                            }
                        it.copy(result = result)
                    }
                }
            }

        private fun inputKey(
            key: PercentKey,
            value: TextFieldValue,
        ): String =
            StringBuilder(value.text).let {
                val index = value.selection.start
                when (key) {
                    PercentKey.BACK -> {
                        if (index == 0) {
                            it.toString()
                        } else {
                            it.delete(index - 1, index).toString()
                        }
                    }
                    else -> it.insert(index, key.value).toString()
                }
            }

        private fun checkDecimal(v: String) = v.any { it == '.' }

        private fun checkDigitsLimit(
            v: String,
            key: PercentKey,
        ): Boolean {
            val count = if (key == PercentKey.ZERO_ZERO) 9 else 10
            return v.replace(".", "").count() >= count
        }
    }
