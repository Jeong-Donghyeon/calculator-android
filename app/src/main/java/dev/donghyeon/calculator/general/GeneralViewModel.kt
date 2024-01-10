package dev.donghyeon.calculator.general

import dev.donghyeon.calculator.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

interface GeneralAction {
    fun inputGeneralSelect(select: GeneralSelect)
}

class GeneralViewModel
    @Inject
    constructor() : BaseViewModel(), GeneralAction {
        private val _state = MutableStateFlow(GeneralData())
        val state = _state.asStateFlow()

        override fun inputGeneralSelect(select: GeneralSelect) {
            _state.value = state.value.copy(select = select)
        }
    }
