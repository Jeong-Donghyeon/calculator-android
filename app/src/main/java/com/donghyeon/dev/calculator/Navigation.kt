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
    val title: Int,
    val icon: Int,
) {
    INFO("Info", R.string.info, R.drawable.ic_info_24px),
    GENERAL("General", R.string.general, R.drawable.ic_general_24px),
    PERCENT("Percent", R.string.percent, R.drawable.ic_percent_24px),
    RATIO("Ratio", R.string.ratio, R.drawable.ic_ratio_24px),
    CONVERT("Convert", R.string.convert, R.drawable.ic_convert_24px),
    DATE("Date", R.string.date, R.drawable.ic_date_24px),
}
