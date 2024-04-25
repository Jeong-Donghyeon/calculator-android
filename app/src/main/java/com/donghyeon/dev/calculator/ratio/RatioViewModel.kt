package com.donghyeon.dev.calculator.ratio

import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.calculate.RatioUseCase
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

interface RatioAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)

    fun inputV1Focus()

    fun inputV2Focus()

    fun inputV3Focus()

    fun inputKey(key: Keyboard)
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
                ratioUseCase(
                    type = type,
                    valueList = newCalculate.valueList.map { it.text },
                ),
        )
    }
}
