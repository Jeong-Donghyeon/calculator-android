package com.donghyeon.dev.calculator.calculate

import java.text.DecimalFormat
import java.util.Calendar
import javax.inject.Inject

enum class DateType {
    DATE_DAY_DATE,
    DATE_DATE_DAY,
    TIME_COMVERT,
}

class DateUseCase @Inject constructor() {

    private val calendar = Calendar.getInstance()
    private val failReturn = "?"

    fun date(
        date: String,
        day: String,
        agoLater: Boolean,
    ): String {
        date.forEach { if (!it.isDigit()) return failReturn }
        day.forEach { if (!it.isDigit()) return failReturn }
        if (date.count() != 8) return failReturn
        if (day.count() > 7) return failReturn
        val cYear = date.substring(0, 4).toIntOrNull() ?: return failReturn
        if (cYear <= 0) return failReturn
        val cMonth = date.substring(4, 6).toIntOrNull()?.let { it - 1 } ?: return failReturn
        if (cMonth < 0) return failReturn
        val cDay = date.substring(6, 8).toIntOrNull() ?: return failReturn
        if (cDay <= 0) return failReturn
        val aDay = day.toIntOrNull() ?: return failReturn
        calendar.set(Calendar.YEAR, cYear)
        calendar.set(Calendar.MONTH, cMonth)
        calendar.set(Calendar.DATE, cDay)
        if (agoLater) {
            calendar.add(Calendar.DATE, aDay)
        } else {
            calendar.add(Calendar.DATE, -aDay)
        }
        val y = DecimalFormat("0000").format(calendar.get(Calendar.YEAR))
        val df = DecimalFormat("00")
        val m = df.format(calendar.get(Calendar.MONTH) + 1)
        val d = df.format(calendar.get(Calendar.DATE))
        return "${y}년 ${m}월 ${d}일"
    }

    fun day(
        date1: String,
        date2: String,
    ): String {
        return ""
    }

    fun time(
        second: String,
        min: String,
        hour: String,
        day: String,
    ): String {
        return ""
    }
}
