package com.donghyeon.dev.calculator.percent

import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.calculate.PercentType
import com.donghyeon.dev.calculator.calculate.PercentUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
import com.donghyeon.dev.calculator.state.Calculate
import com.donghyeon.dev.calculator.view.Keyboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface PercentAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)

    fun inputV1Focus()

    fun inputV2Focus()

    fun inputKey(key: Keyboard)
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

        init {
            viewModelScope.launch {
                val type = PercentType.entries[repository.loadPercentType()]
                _state.value = PercentState(type = type)
            }
        }

        override fun inputType(index: Int) {
            viewModelScope.launch {
                repository.savePercentType(index)
            }
            PercentType.entries.find { it.ordinal == index }?.let {
                _state.value = state.value.copy(type = it)
            }
        }

        override fun inputV1Focus() {
            _state.value =
                state.value.copy(
                    v1Focus = true,
                    v2Focus = false,
                )
        }

        override fun inputV2Focus() {
            _state.value =
                state.value.copy(
                    v1Focus = false,
                    v2Focus = true,
                )
        }

        override fun inputKey(key: Keyboard) {
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

        private fun input(key: Keyboard): Calculate {
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
                    percentUseCase(
                        type = type,
                        value1 = newCalculate.valueList[0].text,
                        value2 = newCalculate.valueList[1].text,
                    ),
            )
        }
    }
