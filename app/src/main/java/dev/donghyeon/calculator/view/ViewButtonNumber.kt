package dev.donghyeon.calculator.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewButtonNumber() =
    ViewButtonNumber(
        text = "0",
        onClick = {},
    )

@Composable
fun ViewButtonNumber(
    modifier: Modifier = Modifier,
    text: String,
    height: Dp = 70.dp,
    size: TextUnit = 24.sp,
    onClick: () -> Unit,
) = Button(
    modifier =
        Modifier
            .height(height)
            .then(modifier),
    shape = RoundedCornerShape(5.dp),
    border = BorderStroke(0.dp, ColorSet.transparent),
    onClick = onClick,
    contentPadding = PaddingValues(),
    colors =
        ButtonDefaults.buttonColors(
            containerColor = ColorSet.button,
            contentColor = ColorSet.text,
        ),
    elevation = null,
) {
    Text(
        text = text,
        style = TextSet.bold.copy(ColorSet.text, size),
    )
}
