package com.donghyeon.dev.calculator

sealed class Navigation {
    data object Pop : Navigation()

    data class PopToDestination(
        val destination: Destination,
    ) : Navigation()

    data class Push(
        val destination: Destination,
    ) : Navigation()

    data class PopPush(
        val destination: Destination,
    ) : Navigation()
}

enum class Destination(val route: String) {
    INTRO("정보"),
    GENERAL("일반 계산기"),
    PERCENT("퍼센트 계산기"),
    CONVERT("단위 변환기"),
}
