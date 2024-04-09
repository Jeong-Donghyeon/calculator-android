package com.donghyeon.dev.calculator.calculate

import javax.inject.Inject

enum class DateType {
    DATE_SEARCH,
    DATE_CONVERT,
    TIME_COMVERT,
}

class DateUseCase
    @Inject
    constructor()
