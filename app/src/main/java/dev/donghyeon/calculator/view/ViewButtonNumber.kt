package dev.donghyeon.calculator.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.common.BUTTON_HEIGHT
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
    height: Dp = BUTTON_HEIGHT.dp,
    size: TextUnit = 24.sp,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val press = interactionSource.collectIsPressedAsState()
    Button(
        modifier =
            Modifier
                .height(height)
                .then(modifier),
        shape = RoundedCornerShape(5.dp),
        onClick = onClick,
        contentPadding = PaddingValues(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = ColorSet.button,
                contentColor = ColorSet.text,
            ),
        elevation = null,
        interactionSource = interactionSource,
    ) {
        val color =
            Text(
                text = text,
                style =
                    TextSet.bold.copy(
                        if (press.value) {
                            ColorSet.select
                        } else {
                            ColorSet.text
                        },
                        size,
                    ),
            )
    }
}
