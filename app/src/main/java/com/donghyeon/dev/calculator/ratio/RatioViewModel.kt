package com.donghyeon.dev.calculator.ratio

import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface RatioAction {
    fun inputType(index: Int)

    fun inputKey(key: RatioKey)
}

@HiltViewModel
class RatioViewModel
    @Inject
    constructor() : BaseViewModel(), RatioAction {
        private val _state = MutableStateFlow(RatioState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        override fun inputType(index: Int) {
            _state.value =
                state.value.let { state ->
                    RatioType.entries.find { it.index == index }?.let {
                        viewModelScope.launch {}
                        state.copy(type = it)
                    } ?: state
                }
        }

        override fun inputKey(key: RatioKey) {}
    }
