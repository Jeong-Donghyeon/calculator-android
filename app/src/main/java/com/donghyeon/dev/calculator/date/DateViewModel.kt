package com.donghyeon.dev.calculator.date

import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface DateAction {
    val sideEffect: SharedFlow<SideEffect>

    fun inputType(index: Int)
}

@HiltViewModel
class DateViewModel
    @Inject
    constructor() : BaseViewModel(), DateAction {
        private val _state = MutableStateFlow(DateState())
        val state = _state.asStateFlow()

        override fun inputType(index: Int) {}
    }
