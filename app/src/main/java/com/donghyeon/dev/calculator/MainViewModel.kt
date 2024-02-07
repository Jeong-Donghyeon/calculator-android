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

        private val _navigationFlow =
            MutableSharedFlow<Triple<Boolean, Destination, Navigation>>()
        val navigationFlow = _navigationFlow.asSharedFlow()

        private val _menuState = MutableStateFlow(false)
        val menuState = _menuState.asStateFlow()

        private val screenStack =
            Stack<Destination>().apply {
                push(StartSceen)
            }

        fun openMenu() {
            _menuState.value = true
        }

        fun closeMenu() {
            _menuState.value = false
        }

        fun navigation(navigation: Navigation) {
            viewModelScope.launch {
                val current = screenStack.peek()
                when (navigation) {
                    is Navigation.Pop -> screenStack.pop()
                    is Navigation.PopToDestination -> {
                        while (true) {
                            val peek = screenStack.peek()
                            if (peek == navigation.destination) {
                                break
                            } else {
                                screenStack.pop()
                            }
                        }
                    }
                    is Navigation.Push -> screenStack.push(navigation.destination)
                    is Navigation.PopPush -> {
                        screenStack.pop()
                        screenStack.push(navigation.destination)
                    }
                }
                _navigationFlow.emit(
                    Triple(
                        screenStack.empty(),
                        current,
                        navigation,
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
