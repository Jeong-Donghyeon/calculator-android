package dev.donghyeon.calculator.percent

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.donghyeon.calculator.common.BaseViewModel
import dev.donghyeon.calculator.common.SideEffect
import dev.donghyeon.calculator.domain.PercentCalculate1UseCase
import dev.donghyeon.calculator.domain.PercentCalculate2UseCase
import dev.donghyeon.calculator.domain.PercentCalculate3UseCase
import dev.donghyeon.calculator.domain.PercentCalculate4UseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PercentAction {
    fun inputPercentSelect(select: PercentSelect)

    fun inputValueSelect(select: ValueSelect)

    fun inputKeyPad(key: PercentKeyPad)
}

@HiltViewModel
class PercentViewModel
    @Inject
    constructor(
        private val percentCalculate1UseCase: PercentCalculate1UseCase,
        private val percentCalculate2UseCase: PercentCalculate2UseCase,
        private val percentCalculate3UseCase: PercentCalculate3UseCase,
        private val percentCalculate4UseCase: PercentCalculate4UseCase,
    ) : BaseViewModel(), PercentAction {
        private val _state = MutableStateFlow(PercentData())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        override fun inputPercentSelect(select: PercentSelect) {
            _state.value = state.value.copy(select = select)
        }

        override fun inputValueSelect(select: ValueSelect) {
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

        override fun inputKeyPad(key: PercentKeyPad) {
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
            key: PercentKeyPad,
            calculate: PercentData.Calculate,
        ): PercentData.Calculate =
            when (key) {
                PercentKeyPad.CLEAR ->
                    when (calculate.select) {
                        ValueSelect.V1 -> calculate.copy(v1 = TextFieldValue(), result = "?")
                        ValueSelect.V2 -> calculate.copy(v2 = TextFieldValue(), result = "?")
                    }
                PercentKeyPad.LEFT ->
                    when (calculate.select) {
                        ValueSelect.V1 -> {
                            val index =
                                calculate.v1.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            calculate.copy(v1 = calculate.v1.copy(selection = TextRange(index)))
                        }
                        ValueSelect.V2 -> {
                            val index =
                                calculate.v2.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            calculate.copy(v2 = calculate.v2.copy(selection = TextRange(index)))
                        }
                    }
                PercentKeyPad.RIGHT ->
                    when (calculate.select) {
                        ValueSelect.V1 -> {
                            val index = calculate.v1.selection.start + 1
                            calculate.copy(v1 = calculate.v1.copy(selection = TextRange(index)))
                        }
                        ValueSelect.V2 -> {
                            val index = calculate.v2.selection.start + 1
                            calculate.copy(v2 = calculate.v2.copy(selection = TextRange(index)))
                        }
                    }
                else -> {
                    val decimalMessage = "소수점은 하나만 입력하세요"
                    val digitsLimitMessage = "최대 10 자리수 입니다"
                    when (calculate.select) {
                        ValueSelect.V1 -> {
                            val decimalCheck = checkDecimal(calculate.v1.text)
                            val digitsLimitCheck = checkDigitsLimit(calculate.v1.text, key)
                            if (key == PercentKeyPad.DECIMAL && decimalCheck) {
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
                                val inputTxt = inputKey(key, calculate.v1)
                                val index =
                                    calculate.v1.selection.start.let {
                                        if (key == PercentKeyPad.BACK) {
                                            if (it == 0) 0 else it - 1
                                        } else {
                                            it + key.value.count()
                                        }
                                    }
                                val v1 =
                                    calculate.v1.copy(
                                        text = inputTxt,
                                        selection = TextRange(index),
                                    )
                                calculate.copy(v1 = v1)
                            }
                        }
                        ValueSelect.V2 -> {
                            val decimalCheck = checkDecimal(calculate.v2.text)
                            val digitsLimitCheck = checkDigitsLimit(calculate.v2.text, key)
                            if (key == PercentKeyPad.DECIMAL && decimalCheck) {
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
                                val inputTxt = inputKey(key, calculate.v2)
                                val index =
                                    calculate.v2.selection.start.let {
                                        if (key == PercentKeyPad.BACK) {
                                            if (it == 0) 0 else it - 1
                                        } else {
                                            it + key.value.count()
                                        }
                                    }
                                val v2 =
                                    calculate.v2.copy(
                                        text = inputTxt,
                                        selection = TextRange(index),
                                    )
                                calculate.copy(v2 = v2)
                            }
                        }
                    }.let {
                        val result =
                            when (state.value.select) {
                                PercentSelect.CALCULATE2 ->
                                    percentCalculate2UseCase(it.v1.text, it.v2.text)
                                PercentSelect.CALCULATE3 ->
                                    percentCalculate3UseCase(it.v1.text, it.v2.text)
                                PercentSelect.CALCULATE4 ->
                                    percentCalculate4UseCase(it.v1.text, it.v2.text)
                                else -> percentCalculate1UseCase(it.v1.text, it.v2.text)
                            }
                        it.copy(result = result)
                    }
                }
            }

        private fun inputKey(
            key: PercentKeyPad,
            value: TextFieldValue,
        ): String =
            StringBuilder(value.text).let {
                val index = value.selection.start
                when (key) {
                    PercentKeyPad.BACK -> {
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
            key: PercentKeyPad,
        ): Boolean {
            val count = if (key == PercentKeyPad.ZERO_ZERO) 9 else 10
            return v.replace(".", "").count() >= count
        }
    }
