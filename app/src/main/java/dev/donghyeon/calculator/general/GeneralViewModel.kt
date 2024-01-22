package dev.donghyeon.calculator.general

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.donghyeon.calculator.calculate.GeneralUseCase
import dev.donghyeon.calculator.common.BaseViewModel
import dev.donghyeon.calculator.common.SideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface GeneralAction {
    fun inputGeneralSelect(select: GeneralSelect)

    fun inputKey(key: GeneralKey)
}

@HiltViewModel
class GeneralViewModel
    @Inject
    constructor(
        private val generalUseCase: GeneralUseCase,
    ) : BaseViewModel(), GeneralAction {
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
                is GeneralKey.CopyResult, GeneralKey.CopyExpress -> calculate
                is GeneralKey.Past -> {
                    val text = calculate.value.text + key.past
                    val selection =
                        calculate.value.selection.let {
                            TextRange(it.start + key.past.count())
                        }
                    val textFieldValue =
                        calculate.value.copy(
                            text = text,
                            selection = selection,
                        )
                    calculate.copy(value = textFieldValue)
                }
                is GeneralKey.Clear -> calculate.copy(value = TextFieldValue(), result = "")
                is GeneralKey.Left -> {
                    val index =
                        calculate.value.selection.start.let {
                            if (it == 0) 0 else it - 1
                        }
                    calculate.copy(value = calculate.value.copy(selection = TextRange(index)))
                }
                is GeneralKey.Right -> {
                    val index = calculate.value.selection.start + 1
                    calculate.copy(value = calculate.value.copy(selection = TextRange(index)))
                }
                else -> {
                    val inputTxt = inputKey(key, calculate.value)
                    val index =
                        calculate.value.selection.start.let {
                            if (key is GeneralKey.Back) {
                                if (it == 0) 0 else it - 1
                            } else {
                                it + key.value.count()
                            }
                        }
                    val v =
                        calculate.value.copy(
                            text = inputTxt,
                            selection = TextRange(index),
                        )
                    calculate.copy(value = v)
                }
            }.let {
                it.copy(result = generalUseCase(it.value.text))
            }

        private fun inputKey(
            key: GeneralKey,
            value: TextFieldValue,
        ): String =
            StringBuilder(value.text).let {
                val index = value.selection.start
                when (key) {
                    is GeneralKey.Back -> {
                        if (index == 0) {
                            it.toString()
                        } else {
                            it.delete(index - 1, index).toString()
                        }
                    }
                    else -> it.insert(index, key.value).toString()
                }
            }
    }
