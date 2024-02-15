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

        private val _bottomMunu = MutableStateFlow(true to StartSceen)
        val bottomMunu = _bottomMunu.asStateFlow()

        private val screenStack =
            Stack<Destination>().apply {
                push(StartSceen)
            }

        fun navigation(next: Navigation) {
            viewModelScope.launch {
                val current = screenStack.peek()
                when (next) {
                    is Navigation.Pop -> screenStack.pop()
                    is Navigation.PopToDestination -> {
                        while (true) {
                            val peek = screenStack.peek()
                            if (peek == next.destination) {
                                break
                            } else {
                                screenStack.pop()
                            }
                        }
                    }
                    is Navigation.Push -> screenStack.push(next.destination)
                    is Navigation.PopPush -> {
                        when (next.destination) {
                            Destination.INFO, Destination.GENERAL, Destination.PERCENT -> {
                                screenStack.pop()
                                screenStack.push(next.destination)
                            }
                            else -> return@launch
                        }
                    }
                }
                if (screenStack.isNotEmpty()) {
                    when (screenStack.peek()) {
                        Destination.MENU, Destination.INFO -> {
                            _bottomMunu.value = bottomMunu.value.copy(false)
                        }
                        else -> {
                            _bottomMunu.value = bottomMunu.value.copy(true, screenStack.peek())
                        }
                    }
                }
                _navigationFlow.emit(
                    Triple(
                        screenStack.empty(),
                        current,
                        next,
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
