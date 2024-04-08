package com.donghyeon.dev.calculator

import androidx.lifecycle.viewModelScope
import com.donghyeon.dev.calculator.common.BaseViewModel
import com.donghyeon.dev.calculator.common.StartSceen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

interface MainAction {
    fun menu(menu: Menu)

    fun navigation(
        nav: Nav,
        dest: Dest?,
    )

    fun showToast(msg: String)
}

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : BaseViewModel(), MainAction {
        private val _toast = MutableSharedFlow<String>()
        val toast = _toast.asSharedFlow()

        private val _navFlow = MutableSharedFlow<Triple<Nav, Dest, Dest?>>()
        val navFlow = _navFlow.asSharedFlow()

        private val screenStack = Stack<Dest>().apply { push(StartSceen) }

        private val _state = MutableStateFlow(MainState())
        val state = _state.asStateFlow()

        init {
            _state.value = state.value.copy(menu = Menu.GENERAL)
        }

        override fun navigation(
            nav: Nav,
            dest: Dest?,
        ) {
            viewModelScope.launch {
                val current = screenStack.peek()
                when (nav) {
                    Nav.EXIT -> {
                        screenStack.clear()
                    }
                    Nav.POP -> {
                        screenStack.pop()
                    }
                    Nav.POP_TO -> {
                        dest?.let {
                            while (true) {
                                if (screenStack.peek() == it) {
                                    break
                                } else {
                                    screenStack.pop()
                                }
                            }
                        }
                    }
                    Nav.POP_ROOT -> {
                        while (true) {
                            if (screenStack.count() > 1) {
                                screenStack.pop()
                            } else {
                                break
                            }
                        }
                    }
                    Nav.PUSH -> {
                        dest?.let {
                            screenStack.push(it)
                        }
                    }
                    Nav.POP_PUSH -> {
                        dest?.let {
                            when (it) {
                                Dest.MAIN,
                                Dest.INFO,
                                -> {
                                    screenStack.pop()
                                    screenStack.push(it)
                                }
                                else -> return@launch
                            }
                        }
                    }
                }
                _navFlow.emit(
                    Triple(
                        nav,
                        current,
                        dest,
                    ),
                )
            }
        }

        override fun showToast(msg: String) {
            viewModelScope.launch {
                _toast.emit(msg)
            }
        }

        override fun menu(menu: Menu) {
            _state.value = state.value.copy(menu = menu)
        }
    }
