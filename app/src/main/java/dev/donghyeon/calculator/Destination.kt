package dev.donghyeon.calculator

sealed class Destination(val route: String) {
    companion object {
        val START_SCREEN = General
    }

    data object Back : Destination("이전")

    data object Info : Destination("정보")

    data object General : Destination("일반 계산기")

    data object Percent : Destination("퍼센트 계산기")
}
