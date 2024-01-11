package dev.donghyeon.calculator.view

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewFieldGeneral() =
    ViewFieldGeneral(
        value = TextFieldValue(text = "1+2+3"),
    )

@Composable
fun ViewFieldGeneral(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
) {
    CompositionLocalProvider(
        LocalTextInputService provides null,
    ) {
        val color = ColorSet.text
        TextField(
            modifier = modifier,
            value = value,
            onValueChange = {},
            textStyle =
                TextSet.extraBold.copy(
                    color = color,
                    fontSize = 26.sp,
                    textAlign = TextAlign.End,
                ),
            colors =
                TextFieldDefaults.colors(
                    focusedTextColor = color,
                    unfocusedTextColor = color,
                    disabledTextColor = color,
                    errorTextColor = color,
                    focusedContainerColor = ColorSet.container,
                    unfocusedContainerColor = ColorSet.container,
                    disabledContainerColor = ColorSet.container,
                    errorContainerColor = ColorSet.container,
                    cursorColor = ColorSet.select,
                    errorCursorColor = ColorSet.select,
                    focusedIndicatorColor = ColorSet.transparent,
                    unfocusedIndicatorColor = ColorSet.transparent,
                    disabledIndicatorColor = ColorSet.transparent,
                    errorIndicatorColor = ColorSet.transparent,
                ),
        )
    }
}
