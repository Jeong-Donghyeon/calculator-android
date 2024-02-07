package com.donghyeon.dev.calculator.convert

import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface ConvertAction

@HiltViewModel
class ConvertViewModel
    @Inject
    constructor() : BaseViewModel(), ConvertAction {
        private val _state = MutableStateFlow(ConvertState())
        val state = _state.asStateFlow()

        private val _sideEffect = MutableSharedFlow<SideEffect>()
        val sideEffect = _sideEffect.asSharedFlow()
    }
