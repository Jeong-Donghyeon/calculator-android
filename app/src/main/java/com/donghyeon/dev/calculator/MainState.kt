package com.donghyeon.dev.calculator

data class MainState(
    val menu: Menu? = null,
)

enum class Menu(
    val title: Int,
    val icon: Int,
) {
    GENERAL(R.string.general, R.drawable.ic_general_24px),
    PERCENT(R.string.percent, R.drawable.ic_percent_24px),
    RATIO(R.string.ratio, R.drawable.ic_ratio_24px),
    CONVERT(R.string.convert, R.drawable.ic_convert_24px),
    DATE(R.string.date, R.drawable.ic_date_24px),
}
