package dev.donghyeon.calculator.common

sealed class SideEffect {
    data class Toast(val message: String) : SideEffect()
}
