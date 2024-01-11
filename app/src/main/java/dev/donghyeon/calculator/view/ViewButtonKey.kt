package dev.donghyeon.calculator.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewButtonKey() {
    ViewButtonKey(
        text = "0",
    )
}

@Composable
fun ViewButtonKey(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val press = interactionSource.collectIsPressedAsState()
    val color = if (press.value) ColorSet.select else ColorSet.text
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        onClick = onClick ?: {},
        contentPadding = PaddingValues(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = ColorSet.button,
                contentColor = ColorSet.text,
            ),
        elevation = null,
        interactionSource = interactionSource,
    ) {
        Text(
            text = text,
            style = TextSet.bold.copy(color, 24.sp),
        )
    }
}
