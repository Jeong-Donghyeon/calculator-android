package dev.donghyeon.calculator.calculate

import javax.inject.Inject

class GeneralUseCase
    @Inject
    constructor(
        private val formatNumber: FormatNumber,
    ) {
        operator fun invoke(input: String): String {
            return "?"
        }
    }
