package com.donghyeon.dev.calculator

data class MainState(
    val menu: Menu? = null,
)

enum class Menu(val icon: Int) {
    GENERAL(R.drawable.ic_general_24px),
    PERCENT(R.drawable.ic_percent_24px),
    RATIO(R.drawable.ic_ratio_24px),
    DATE(R.drawable.ic_date_24px),
    CONVERT(R.drawable.ic_convert_24px),
}
