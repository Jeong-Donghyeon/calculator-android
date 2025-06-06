package com.donghyeon.dev.calculator.view

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.common.DisableSoftKeyboard
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewFieldNumber() {
    ViewFieldNumber(
        value = TextFieldValue(text = "123.123"),
        color = ColorSet.select,
    )
}

@Composable
fun ViewFieldNumber(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    color: Color,
    align: TextAlign = TextAlign.Center,
) {
    DisableSoftKeyboard {
        TextField(
            modifier = modifier,
            value = value,
            onValueChange = {},
            textStyle =
                TextSet.extraBold.copy(
                    color = color,
                    fontSize = 26.sp,
                    textAlign = align,
                ),
            colors =
                TextFieldDefaults.colors(
                    focusedTextColor = color,
                    unfocusedTextColor = color,
                    disabledTextColor = color,
                    errorTextColor = color,
                    focusedContainerColor = ColorSet.background,
                    unfocusedContainerColor = ColorSet.background,
                    disabledContainerColor = ColorSet.background,
                    errorContainerColor = ColorSet.background,
                    cursorColor = ColorSet.select,
                    errorCursorColor = ColorSet.select,
                    focusedIndicatorColor = color,
                    unfocusedIndicatorColor = color,
                    disabledIndicatorColor = color,
                    errorIndicatorColor = color,
                ),
        )
    }
}
