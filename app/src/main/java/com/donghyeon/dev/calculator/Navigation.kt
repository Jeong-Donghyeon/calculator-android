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

enum class Destination(
    val route: String,
    val icon: Int,
) {
    MENU("메뉴", R.drawable.ic_menu_24px),
    INFO("정보", R.drawable.ic_info_24px),
    GENERAL("일반 계산", R.drawable.ic_general_24px),
    PERCENT("퍼센트 계산", R.drawable.ic_percent_24px),
    RATIO("비율 계산", R.drawable.ic_ratio_24px),
    CONVERT("단위 계산", R.drawable.ic_convert_24px),
    CURRENCY("환율 계산", R.drawable.ic_currency_24px),
    DATE("날짜 계산", R.drawable.ic_date_24px),
}
