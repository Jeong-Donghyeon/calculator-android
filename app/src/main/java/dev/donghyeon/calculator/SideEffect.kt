package dev.donghyeon.calculator

sealed class SideEffect {
    data class Toast(val message: String) : SideEffect()
}
