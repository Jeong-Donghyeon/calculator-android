package com.donghyeon.dev.calculator.date

import androidx.compose.ui.text.TextRange
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.calculate.DateType
import com.donghyeon.dev.calculator.calculate.DateUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

interface DateAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)

    fun inputKey(key: DateKey)
}

@HiltViewModel
class DateViewModel @Inject constructor(
    private val repository: Repository,
    private val dateUseCase: DateUseCase,
) : BaseViewModel(), DateAction {
    private val _state = MutableStateFlow(DateState())
    val state = _state.asStateFlow()

    private val _dateDayDateState = MutableStateFlow(DateDayDateState())
    val dateDayDateState = _dateDayDateState.asStateFlow()

    private val _dateDateDayState = MutableStateFlow(DateDateDayState())
    val dateDateDayState = _dateDateDayState.asStateFlow()

    init {
        viewModelScope.launch {
            val type = DateType.entries[repository.loadDateType()]
            _state.value =
                DateState(
                    type = type,
                    hint = getToday(),
                )
        }
    }

    override fun inputType(index: Int) {
        viewModelScope.launch {
            repository.saveDateType(index)
        }
        DateType.entries.find { it.ordinal == index }?.let {
            _state.value = state.value.copy(type = it)
        }
    }

    override fun inputKey(key: DateKey) {
        val state = state.value
        when (state.type) {
            DateType.DATE_DAY_DATE -> inputDateDayDate(key)
            else -> {}
        }
    }

    private fun inputDateDayDate(key: DateKey) {
        val state = dateDayDateState.value
        val inputState =
            when (key) {
                is DateKey.Clear -> DateDayDateState()
                is DateKey.Left -> {
                    when (state.focus) {
                        DateDayDateState.Focus.DATE -> {
                            val index =
                                state.date.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            state.copy(date = state.date.copy(selection = TextRange(index)))
                        }
                        DateDayDateState.Focus.DAY -> {
                            val index =
                                state.day.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            state.copy(day = state.day.copy(selection = TextRange(index)))
                        }
                        DateDayDateState.Focus.AGO_LATER -> state.copy(agoLater = false)
                    }
                }
                is DateKey.Right -> {
                    when (state.focus) {
                        DateDayDateState.Focus.DATE -> {
                            val index = state.date.selection.start + 1
                            state.copy(date = state.date.copy(selection = TextRange(index)))
                        }
                        DateDayDateState.Focus.DAY -> {
                            val index = state.day.selection.start + 1
                            state.copy(day = state.day.copy(selection = TextRange(index)))
                        }
                        DateDayDateState.Focus.AGO_LATER -> state.copy(agoLater = true)
                    }
                }
                is DateKey.Backspace -> {
                    when (state.focus) {
                        DateDayDateState.Focus.DATE -> {
                            val text =
                                StringBuilder(state.date.text).let {
                                    val index = state.date.selection.start
                                    if (index == 0) {
                                        it.toString()
                                    } else {
                                        it.delete(index - 1, index).toString()
                                    }
                                }
                            val index =
                                state.date.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            state.copy(date = state.date.copy(text = text, selection = TextRange(index)))
                        }
                        DateDayDateState.Focus.DAY -> {
                            val text =
                                StringBuilder(state.day.text).let {
                                    val index = state.day.selection.start
                                    if (index == 0) {
                                        it.toString()
                                    } else {
                                        it.delete(index - 1, index).toString()
                                    }
                                }
                            val index =
                                state.day.selection.start.let {
                                    if (it == 0) 0 else it - 1
                                }
                            state.copy(day = state.day.copy(text = text, selection = TextRange(index)))
                        }
                        DateDayDateState.Focus.AGO_LATER -> state
                    }
                }
                is DateKey.Copy -> state
                is DateKey.Paste -> {
                    val result = key.result.replace(",", "")
                    when (state.focus) {
                        DateDayDateState.Focus.DATE -> {
                            state.copy(date = state.date.copy(text = result, selection = TextRange(result.length)))
                        }
                        DateDayDateState.Focus.DAY -> {
                            state.copy(day = state.day.copy(text = result, selection = TextRange(result.length)))
                        }
                        DateDayDateState.Focus.AGO_LATER -> state
                    }
                }
                is DateKey.Enter -> {
                    when (state.focus) {
                        DateDayDateState.Focus.DATE -> state.copy(focus = DateDayDateState.Focus.DAY)
                        DateDayDateState.Focus.DAY -> state.copy(focus = DateDayDateState.Focus.AGO_LATER)
                        DateDayDateState.Focus.AGO_LATER -> state.copy(focus = DateDayDateState.Focus.DATE)
                    }
                }
                is DateKey.Today -> {
                    val today = getToday()
                    this._state.value = this.state.value.copy(hint = today)
                    when (state.focus) {
                        DateDayDateState.Focus.DATE -> {
                            state.copy(date = state.date.copy(text = today, selection = TextRange(today.length)))
                        }
                        DateDayDateState.Focus.DAY -> {
                            state.copy(date = state.day.copy(text = today, selection = TextRange(today.length)))
                        }
                        DateDayDateState.Focus.AGO_LATER -> state
                    }
                }
                is DateKey.ZeroZero, DateKey.Zero,
                DateKey.One, DateKey.Two, DateKey.Three,
                DateKey.Four, DateKey.Five, DateKey.Six,
                DateKey.Seven, DateKey.Eight, DateKey.Nine,
                -> {
                    when (state.focus) {
                        DateDayDateState.Focus.DATE -> {
                            val valueCount = state.date.text.count()
                            val digitsLimitCheck =
                                if (key == DateKey.ZeroZero) {
                                    valueCount >= 7
                                } else {
                                    valueCount >= 8
                                }
                            if (digitsLimitCheck) {
                                state
                            } else {
                                val text =
                                    StringBuilder(state.date.text).let {
                                        val index = state.date.selection.start
                                        it.insert(index, key.value).toString()
                                    }
                                val index =
                                    state.date.selection.start.let {
                                        it + key.value.count()
                                    }
                                state.copy(date = state.date.copy(text = text, selection = TextRange(index)))
                            }
                        }
                        DateDayDateState.Focus.DAY -> {
                            val valueCount = state.day.text.count()
                            val digitsLimitCheck =
                                if (key == DateKey.ZeroZero) {
                                    valueCount >= 9
                                } else {
                                    valueCount >= 10
                                }
                            if (digitsLimitCheck) {
                                state
                            } else {
                                val text =
                                    StringBuilder(state.day.text).let {
                                        val index = state.day.selection.start
                                        it.insert(index, key.value).toString()
                                    }
                                val index =
                                    state.day.selection.start.let {
                                        it + key.value.count()
                                    }
                                state.copy(day = state.day.copy(text = text, selection = TextRange(index)))
                            }
                        }
                        DateDayDateState.Focus.AGO_LATER -> state
                    }
                }
            }
        val result =
            dateUseCase.date(
                date = inputState.date.text,
                day = inputState.day.text,
                agoLater = inputState.agoLater,
            )
        _dateDayDateState.value = inputState.copy(result = result)
    }

    private fun getToday(): String {
        return SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
    }
}
