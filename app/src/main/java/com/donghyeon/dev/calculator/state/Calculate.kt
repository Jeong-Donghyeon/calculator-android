package com.donghyeon.dev.calculator.state

import androidx.compose.ui.text.input.TextFieldValue

data class Calculate(
    val valueList: List<TextFieldValue>,
    val result: String = "?",
)
