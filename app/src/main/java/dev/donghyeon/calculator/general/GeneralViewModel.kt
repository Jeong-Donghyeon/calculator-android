package dev.donghyeon.calculator.general

import dev.donghyeon.calculator.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface GeneralAction {
    fun input(s: String)
}

class GeneralViewModel @Inject constructor() : BaseViewModel(), GeneralAction {

    private val _generalState = MutableStateFlow(GeneralData())
    val generalState: StateFlow<GeneralData> = _generalState

    override fun input(s: String)  {
        _generalState.value = generalState.value.let {
            it.copy(calculation = it.calculation + s)
        }
    }
}
