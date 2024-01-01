package dev.donghyeon.calculator.common

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewButtonNumber() = ViewButtonNumber(
    text = "0",
    onClick = {},
)

@Composable
fun ViewButtonNumber(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) = Button(
    modifier = Modifier
        .height(70.dp)
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
        style = TextSet.bold.copy(ColorSet.text, 24.sp),
    )
}
