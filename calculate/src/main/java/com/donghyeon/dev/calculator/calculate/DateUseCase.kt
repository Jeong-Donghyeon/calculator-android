package com.donghyeon.dev.calculator.calculate

import javax.inject.Inject

enum class DateType {
    DATE_DAY_DATE,
    DATE_DATE_DAY,
    TIME_COMVERT,
}

class DateUseCase @Inject constructor()
