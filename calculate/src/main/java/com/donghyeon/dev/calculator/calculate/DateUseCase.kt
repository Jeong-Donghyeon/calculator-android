package com.donghyeon.dev.calculator.calculate

import java.text.DecimalFormat
import java.util.Calendar
import javax.inject.Inject

enum class DateType {
    DATE_DAY_DATE,
    DATE_DATE_DAY,
}

class DateUseCase @Inject constructor() {

    private val calendar =
        Calendar.getInstance().apply {
            isLenient = false
        }
    private val failReturn = "?"

    fun date(
        date: String,
        day: String,
        agoLater: Boolean,
    ): String {
        try {
            val dateCalendar = dateToCalendar(date) ?: return failReturn
            val dayInt = dayToInt(day) ?: return failReturn
            if (agoLater) {
                dateCalendar.add(Calendar.DATE, dayInt)
            } else {
                dateCalendar.add(Calendar.DATE, -dayInt)
            }
            val y = DecimalFormat("0000").format(dateCalendar.get(Calendar.YEAR))
            val df = DecimalFormat("00")
            val m = df.format(dateCalendar.get(Calendar.MONTH) + 1)
            val d = df.format(dateCalendar.get(Calendar.DATE))
            return "${y}년 ${m}월 ${d}일"
        } catch (e: Exception) {
            return failReturn
        }
    }

    fun day(
        date1: String,
        date2: String,
    ): String {
        try {
            val date1Time = dateToCalendar(date1)?.timeInMillis ?: return failReturn
            val date2Time = dateToCalendar(date2)?.timeInMillis ?: return failReturn
            return ((date2Time - date1Time) / 86400000).toString() + " 일"
        } catch (e: Exception) {
            return failReturn
        }
    }

    private fun dateToCalendar(date: String): Calendar? {
        date.forEach { if (!it.isDigit()) return null }
        if (date.count() != 8) return null
        val cYear = date.substring(0, 4).toIntOrNull() ?: return null
        if (cYear <= 0) return null
        val cMonth = date.substring(4, 6).toIntOrNull()?.let { it - 1 } ?: return null
        if (cMonth < 0) return null
        val cDay = date.substring(6, 8).toIntOrNull() ?: return null
        if (cDay <= 0) return null
        calendar.set(Calendar.YEAR, cYear)
        calendar.set(Calendar.MONTH, cMonth)
        calendar.set(Calendar.DATE, cDay)
        return calendar
    }

    private fun dayToInt(day: String): Int? {
        day.forEach { if (!it.isDigit()) return null }
        if (day.count() > 7) return null
        return day.toIntOrNull()
    }
}
