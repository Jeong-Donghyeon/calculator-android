package com.donghyeon.dev.calculator.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.common.DisableSoftKeyboard
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewFieldDate() {
    ViewFieldDate(
        value = TextFieldValue(text = "19700101"),
        color = ColorSet.select,
    )
}

@Preview
@Composable
fun Preview_ViewFieldDate_Hint() {
    ViewFieldDate(hint = "19700101")
}

@Composable
fun ViewFieldDate(
    focus: FocusRequester = FocusRequester(),
    value: TextFieldValue = TextFieldValue(),
    hint: String = "",
    color: Color = ColorSet.text,
) {
    DisableSoftKeyboard {
        TextField(
            modifier =
                Modifier
                    .width(220.dp)
                    .focusRequester(focus),
            value = value,
            placeholder = {
                Text(
                    text = hint,
                    style =
                        TextSet.extraBold.copy(
                            color = ColorSet.hint,
                            fontSize = 26.sp,
                        ),
                )
            },
            prefix = {
                Spacer(modifier = Modifier.width(22.dp))
            },
            suffix = {
                Spacer(modifier = Modifier.width(22.dp))
            },
            onValueChange = {},
            textStyle =
                TextSet.extraBold.copy(
                    color = color,
                    fontSize = 26.sp,
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
