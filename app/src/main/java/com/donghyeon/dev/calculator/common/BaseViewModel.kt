package com.donghyeon.dev.calculator.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseViewModel : ViewModel() {
    val sideEffect = MutableSharedFlow<SideEffect>()
}
