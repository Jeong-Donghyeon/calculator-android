package com.donghyeon.dev.calculator.convert

import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.calculate.ConvertType
import com.donghyeon.dev.calculator.calculate.ConvertUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ConvertAction {
    fun inputType(index: Int)

    fun sheet(enable: Boolean)

    fun inputKey(key: ConvertKey)
}

@HiltViewModel
class ConvertViewModel
    @Inject
    constructor(
        private val convertUseCase: ConvertUseCase,
    ) : BaseViewModel(), ConvertAction {
        private val _state = MutableStateFlow(ConvertState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()

        override fun inputType(index: Int) {
            _state.value =
                state.value.let { state ->
                    ConvertType.entries.find { it.ordinal == index }?.let {
                        viewModelScope.launch {}
                        state.copy(type = it)
                    } ?: state
                }
        }

        override fun sheet(enable: Boolean) {
            _state.value = state.value.copy(sheet = enable)
        }

        override fun inputKey(key: ConvertKey) {}
    }
