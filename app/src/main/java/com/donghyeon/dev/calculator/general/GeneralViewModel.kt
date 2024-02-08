package com.donghyeon.dev.calculator.general

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.donghyeon.dev.calculator.calculate.GeneralUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface GeneralAction {
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

        override fun inputKey(key: GeneralKey) {
            val state = state.value
            _state.value =
                when (key) {
                    is GeneralKey.Copy -> state
                    is GeneralKey.Paste -> {
                        val text = state.value.text + key.paste
                        val selection =
                            state.value.selection.let {
                                TextRange(it.start + key.paste.count())
                            }
                        val textFieldValue =
                            state.value.copy(
                                text = text,
                                selection = selection,
                            )
                        state.copy(value = textFieldValue)
                    }
                    is GeneralKey.Clear -> state.copy(value = TextFieldValue(), result = "")
                    is GeneralKey.Left -> {
                        val index =
                            state.value.selection.start.let {
                                if (it == 0) 0 else it - 1
                            }
                        state.copy(value = state.value.copy(selection = TextRange(index)))
                    }
                    is GeneralKey.Right -> {
                        val index = state.value.selection.start + 1
                        state.copy(value = state.value.copy(selection = TextRange(index)))
                    }
                    else -> {
                        val inputTxt = inputKey(key, state.value)
                        val index =
                            state.value.selection.start.let {
                                if (key is GeneralKey.Backspace) {
                                    if (it == 0) 0 else it - 1
                                } else {
                                    it + key.value.count()
                                }
                            }
                        val v =
                            state.value.copy(
                                text = inputTxt,
                                selection = TextRange(index),
                            )
                        state.copy(value = v)
                    }
                }.let {
                    it.copy(result = generalUseCase(it.value.text))
                }
        }

        private fun inputKey(
            key: GeneralKey,
            value: TextFieldValue,
        ): String =
            StringBuilder(value.text).let {
                val index = value.selection.start
                when (key) {
                    is GeneralKey.Backspace -> {
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