package com.donghyeon.dev.calculator

enum class Nav {
    EXIT,
    POP,
    POP_ROOT,
    POP_TO,
    PUSH,
    POP_PUSH,
}

enum class Dest(
    val route: String,
    val title: String,
    val icon: Int,
) {
    INFO("Info", "정보", R.drawable.ic_info_24px),
    GENERAL("General", "일반 계산", R.drawable.ic_general_24px),
    PERCENT("Percent", "퍼센트 계산", R.drawable.ic_percent_24px),
    RATIO("Ratio", "비율 계산", R.drawable.ic_ratio_24px),
    CONVERT("Convert", "단위 계산", R.drawable.ic_convert_24px),
    CURRENCY("Currency", "환율 계산", R.drawable.ic_currency_24px),
    DATE("Date", "날짜 계산", R.drawable.ic_date_24px),
}
