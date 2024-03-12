package com.donghyeon.dev.calculator.calculate

import javax.inject.Inject

enum class ConvertType(val index: Int) {
    LENGTH(0),
}

enum class ConvertUnit(val value: String) {
    CM("cm"),
    M("m"),
}

class ConvertUseCase
    @Inject
    constructor() {
        private val defaultValue: String = "?"

        operator fun invoke(
            type: ConvertType,
            value1: String,
            value2: String,
        ): String = "?"
    }
