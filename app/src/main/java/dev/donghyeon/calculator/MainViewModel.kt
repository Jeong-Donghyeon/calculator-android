package dev.donghyeon.calculator

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.donghyeon.calculator.common.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : BaseViewModel() {
        private val _toast = MutableSharedFlow<String>()
        val toast = _toast.asSharedFlow()

        private val _nav = MutableSharedFlow<Pair<Destination, Destination>>()
        val nav = _nav.asSharedFlow()

        private val _menu = MutableStateFlow(false)
        val menu = _menu.asStateFlow()

        private var currentScreen: Destination = Destination.Percent

        fun openMenu() {
            _menu.value = true
        }

        fun closeMenu() {
            _menu.value = false
        }

        fun nav(destination: Destination) {
            viewModelScope.launch {
                _nav.emit(currentScreen to destination)
                currentScreen = destination
            }
        }

        fun showToast(message: String) {
            viewModelScope.launch {
                _toast.emit(message)
            }
        }
    }
