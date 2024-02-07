package com.donghyeon.dev.calculator.view

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet

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
    icon: Pair<Int, Dp>? = null,
    onClick: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val press = interactionSource.collectIsPressedAsState()
    val pressColor = if (press.value) ColorSet.select else ColorSet.text
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        onClick = onClick ?: {},
        contentPadding = PaddingValues(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = ColorSet.button,
            ),
        elevation = null,
        interactionSource = interactionSource,
    ) {
        if (icon == null) {
            Text(
                text = text,
                style = TextSet.bold.copy(pressColor, 24.sp),
            )
        } else {
            Icon(
                modifier =
                    Modifier
                        .padding(top = 1.dp)
                        .size(icon.second),
                painter = painterResource(id = icon.first),
                tint = pressColor,
                contentDescription = "KeyIcon",
            )
        }
    }
}
