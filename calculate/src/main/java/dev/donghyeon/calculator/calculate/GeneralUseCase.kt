package dev.donghyeon.calculator.calculate

import javax.inject.Inject

class GeneralUseCase
    @Inject
    constructor(
        private val formatNumber: FormatNumber,
        private val formatPostfix: FormatPostfix,
    ) {
        operator fun invoke(input: String): String {
            return "?"
        }
    }
