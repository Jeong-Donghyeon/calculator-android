package com.donghyeon.dev.calculator.convert

import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.calculate.ConvertType
import com.donghyeon.dev.calculator.calculate.ConvertUseCase
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ConvertAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)

    fun sheet(enable: Boolean)

    fun inputKey(key: ConvertKey)
}

@HiltViewModel
class ConvertViewModel
    @Inject
    constructor(
        private val convertUseCase: ConvertUseCase,
        private val repository: Repository,
    ) : BaseViewModel(), ConvertAction {
        private val _state = MutableStateFlow(ConvertState())
        val state = _state.asStateFlow()

        init {
            viewModelScope.launch {
                val type = ConvertType.entries[repository.loadConvertType()]
                _state.value = ConvertState(type = type)
            }
        }

        override fun inputType(index: Int) {
            viewModelScope.launch {
                repository.saveConvertType(index)
            }
            ConvertType.entries.find { it.ordinal == index }?.let {
                _state.value = state.value.copy(type = it)
            }
        }

        override fun sheet(enable: Boolean) {
            _state.value = state.value.copy(sheet = enable)
        }

        override fun inputKey(key: ConvertKey) {}
    }
