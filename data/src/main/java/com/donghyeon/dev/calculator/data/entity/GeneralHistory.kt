package com.donghyeon.dev.calculator.data.entity

data class GeneralHistory(
    val historyList: List<History>,
) {
    data class History(
        val date: Long,
        val express: String,
        val result: String,
    )
}
