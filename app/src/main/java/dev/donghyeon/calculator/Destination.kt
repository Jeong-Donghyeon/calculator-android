package dev.donghyeon.calculator

sealed class Destination(val route: String) {
    data object General : Destination("일반 계산기")

    data object Percent : Destination("퍼센트 계산기")
}
