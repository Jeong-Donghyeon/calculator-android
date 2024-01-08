package dev.donghyeon.calculator

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : BaseViewModel() {
        private val _toast = MutableSharedFlow<String>()
        val toast = _toast.asSharedFlow()

        fun showToast(message: String) {
            viewModelScope.launch {
                _toast.emit(message)
            }
        }
    }
