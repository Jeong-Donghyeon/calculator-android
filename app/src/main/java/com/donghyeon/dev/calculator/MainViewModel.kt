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

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : BaseViewModel() {
        private val _toast = MutableSharedFlow<String>()
        val toast = _toast.asSharedFlow()

        private val _navFlow = MutableSharedFlow<Triple<Nav, Dest, Dest?>>()
        val navFlow = _navFlow.asSharedFlow()

        private val _bottomMunu = MutableStateFlow(true to StartSceen)
        val bottomMunu = _bottomMunu.asStateFlow()

        private val screenStack = Stack<Dest>().apply { push(StartSceen) }

        fun navigation(
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
                                Dest.INFO, Dest.GENERAL, Dest.PERCENT -> {
                                    screenStack.pop()
                                    screenStack.push(it)
                                }
                                else -> return@launch
                            }
                        }
                    }
                }
                if (screenStack.isNotEmpty()) {
                    if (screenStack.peek() == Dest.INFO) {
                        _bottomMunu.value = bottomMunu.value.copy(false)
                    } else {
                        _bottomMunu.value = bottomMunu.value.copy(true, screenStack.peek())
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

        fun showToast(message: String) {
            viewModelScope.launch {
                _toast.emit(message)
            }
        }
    }
