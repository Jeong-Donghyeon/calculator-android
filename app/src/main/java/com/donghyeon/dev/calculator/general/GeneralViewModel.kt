package com.donghyeon.dev.calculator.general

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.GeneralUseCase
import com.donghyeon.dev.calculator.calculate.GenralOperator
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
import com.donghyeon.dev.calculator.data.entity.GeneralHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

interface GeneralAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputKey(key: GeneralKey)

    fun history()

    fun clearHistory()
}

@HiltViewModel
class GeneralViewModel @Inject constructor(
    private val generalUseCase: GeneralUseCase,
    private val repository: Repository,
) : BaseViewModel(), GeneralAction {
    private val _state: MutableStateFlow<GeneralState> = MutableStateFlow(GeneralState())
    val state: StateFlow<GeneralState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.generalHistory.collectLatest { generalHistory ->
                val historyList =
                    generalHistory.historyList.map {
                        it.express to it.result
                    }
                _state.value = state.value.copy(historyList = historyList)
            }
        }
    }

    override fun inputKey(key: GeneralKey) {
        val state = state.value
        _state.value =
            when (key) {
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
                is GeneralKey.Copy -> state
                is GeneralKey.Paste -> {
                    val copyStrCheck =
                        key.result.all { c ->
                            listOf(
                                GeneralKey.Decimal.value,
                                GeneralKey.Zero.value,
                                GeneralKey.One.value,
                                GeneralKey.Two.value,
                                GeneralKey.Three.value,
                                GeneralKey.Four.value,
                                GeneralKey.Five.value,
                                GeneralKey.Six.value,
                                GeneralKey.Seven.value,
                                GeneralKey.Eight.value,
                                GeneralKey.Nine.value,
                            ).any {
                                it == c.toString()
                            }
                        }
                    if (copyStrCheck) {
                        val text = state.value.text + key.result
                        val selection =
                            state.value.selection.let {
                                TextRange(it.start + key.result.count())
                            }
                        val textFieldValue =
                            state.value.copy(
                                text = text,
                                selection = selection,
                            )
                        state.copy(value = textFieldValue)
                    } else {
                        state
                    }
                }
                is GeneralKey.Open, GeneralKey.Close -> {
                    val text = state.value.text
                    val index = state.value.selection.start
                    val textFront = text.substring(0, index)
                    val textBack = text.substring(index, text.lastIndex + 1)
                    state.copy(
                        value =
                            TextFieldValue(
                                text =
                                    if (key == GeneralKey.Open) {
                                        textFront + GenralOperator.OPEN.value + textBack
                                    } else {
                                        textFront + GenralOperator.CLOSE.value + textBack
                                    },
                                selection = TextRange(index + 1),
                            ),
                    )
                }
                is GeneralKey.Equal -> {
                    if (state.result == "") {
                        state
                    } else {
                        val value =
                            state.result.replace(",", "").let {
                                if (it.first().toString() == GenralOperator.MINUS.value) {
                                    "($it"
                                } else {
                                    it
                                }
                            }
                        viewModelScope.launch {
                            repository.saveGeneralHistory(
                                GeneralHistory.History(
                                    date = System.currentTimeMillis(),
                                    express = state.value.text,
                                    result = state.result,
                                ),
                            )
                        }
                        state.copy(value = state.value.copy(text = value))
                    }
                }
                else -> {
                    val checkOperator =
                        listOf(
                            GeneralKey.Plus,
                            GeneralKey.Minus,
                            GeneralKey.Multiply,
                            GeneralKey.Divide,
                        ).any { it == key }
                    if (state.value.text == "" && checkOperator) return
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

    override fun history() {
        val state = state.value
        viewModelScope.launch {
            if (state.historyList.isEmpty()) {
                sideEffect.emit(SideEffect.Toast(R.string.error_history))
            } else {
                _state.value =
                    state.copy(
                        history = !state.history,
                        historyList = state.historyList,
                    )
            }
        }
    }

    override fun clearHistory() {
        _state.value = state.value.copy(history = false)
        viewModelScope.launch {
            repository.clearGeneralHistory()
        }
    }

    private fun inputKey(
        key: GeneralKey,
        value: TextFieldValue,
    ): String {
        return StringBuilder(value.text).let {
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
}
