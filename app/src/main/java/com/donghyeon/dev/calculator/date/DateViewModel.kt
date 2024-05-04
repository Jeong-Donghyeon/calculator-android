package com.donghyeon.dev.calculator.date

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.calculate.DateType
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

interface DateAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)
}

@HiltViewModel
class DateViewModel @Inject constructor(
    private val repository: Repository,
) : BaseViewModel(), DateAction {
    private val _state = MutableStateFlow(DateState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val type = DateType.entries[repository.loadRatioType()]
            val date =
                SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                    .format(Calendar.getInstance().time)
            _state.value =
                DateState(
                    type = type,
                    dateSearch =
                        DateState.DateSearch(
                            date =
                                TextFieldValue(
                                    date,
                                    TextRange(date.length),
                                ),
                        ),
                )
        }
    }

    override fun inputType(index: Int) {
        viewModelScope.launch {
            repository.saveConvertType(index)
        }
        DateType.entries.find { it.ordinal == index }?.let {
            _state.value = state.value.copy(type = it)
        }
    }
}
