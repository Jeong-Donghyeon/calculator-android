package com.donghyeon.dev.calculator.convert

import androidx.compose.ui.text.input.TextFieldValue

data class ConvertState(
    val convert1: Convert = Convert(),
) {
    data class Convert(
        val value: TextFieldValue = TextFieldValue(),
        val result: String = "?",
    )
}
