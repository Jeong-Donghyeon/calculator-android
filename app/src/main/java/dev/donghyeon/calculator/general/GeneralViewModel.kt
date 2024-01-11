package dev.donghyeon.calculator.general

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import dev.donghyeon.calculator.common.BaseViewModel
import dev.donghyeon.calculator.common.SideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface GeneralAction {
    fun inputGeneralSelect(select: GeneralSelect)

    fun inputKey(key: GeneralKey)
}

class GeneralViewModel
    @Inject
    constructor() : BaseViewModel(), GeneralAction {
        private val _state = MutableStateFlow(GeneralState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        override fun inputGeneralSelect(select: GeneralSelect) {
            _state.value = state.value.copy(select = select)
        }

        override fun inputKey(key: GeneralKey) {
            _state.value =
                state.value.let {
                    when (it.select) {
                        GeneralSelect.CALCULATE2 ->
                            it.copy(calculate2 = input(key, it.calculate2))
                        GeneralSelect.CALCULATE3 ->
                            it.copy(calculate3 = input(key, it.calculate3))
                        GeneralSelect.CALCULATE4 ->
                            it.copy(calculate4 = input(key, it.calculate4))
                        else -> it.copy(calculate1 = input(key, it.calculate1))
                    }
                }
        }

        private fun input(
            key: GeneralKey,
            calculate: GeneralState.Calculate,
        ): GeneralState.Calculate =
            when (key) {
                GeneralKey.CLEAR -> calculate.copy(v = TextFieldValue(), result = "?")
                GeneralKey.LEFT -> {
                    val index =
                        calculate.v.selection.start.let {
                            if (it == 0) 0 else it - 1
                        }
                    calculate.copy(v = calculate.v.copy(selection = TextRange(index)))
                }
                GeneralKey.RIGHT -> {
                    val index = calculate.v.selection.start + 1
                    calculate.copy(v = calculate.v.copy(selection = TextRange(index)))
                }
                else -> {
                    val decimalMessage = "소수점은 하나만 입력하세요"
                    val decimalCheck = checkDecimal(calculate.v.text)
                    if (key == GeneralKey.DECIMAL && decimalCheck) {
                        viewModelScope.launch {
                            _sideEffect.emit(SideEffect.Toast(decimalMessage))
                        }
                        calculate
                    } else {
                        val inputTxt = inputKey(key, calculate.v)
                        val index =
                            calculate.v.selection.start.let {
                                if (key == GeneralKey.BACK) {
                                    if (it == 0) 0 else it - 1
                                } else {
                                    it + key.value.count()
                                }
                            }
                        val v =
                            calculate.v.copy(
                                text = inputTxt,
                                selection = TextRange(index),
                            )
                        calculate.copy(v = v)
                    }
                }
            }

        private fun inputKey(
            key: GeneralKey,
            value: TextFieldValue,
        ): String =
            StringBuilder(value.text).let {
                val index = value.selection.start
                when (key) {
                    GeneralKey.BACK -> {
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
    }
