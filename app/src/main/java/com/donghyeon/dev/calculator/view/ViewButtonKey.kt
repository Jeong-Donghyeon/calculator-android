package com.donghyeon.dev.calculator.view

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.calculate.GenralOperator
import com.donghyeon.dev.calculator.general.GeneralKey
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewButtonKey() {
    ViewButtonKey(
        text = "0",
    )
}

@Preview
@Composable
fun Preview_ViewButtonKey_Icon() {
    ViewButtonKey(
        icon = GeneralKey.Backspace.value.toInt() to 32.dp,
    )
}

@Composable
fun ViewButtonKey(
    modifier: Modifier = Modifier,
    text: String = "",
    icon: Pair<Int, Dp>? = null,
    onClick: (() -> Unit) = {},
) {
    val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
    val interactionSource = remember { MutableInteractionSource() }
    val press = interactionSource.collectIsPressedAsState()
    val pressColor = if (press.value) ColorSet.select else ColorSet.text
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        10,
                        VibrationEffect.DEFAULT_AMPLITUDE,
                    ),
                )
            } else {
                vibrator.vibrate(10)
            }
            onClick()
        },
        contentPadding = PaddingValues(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = ColorSet.button,
            ),
        elevation = null,
        interactionSource = interactionSource,
    ) {
        if (icon == null) {
            val operator =
                listOf(
                    GenralOperator.MULTIPLY.value,
                    GenralOperator.DIVIDE.value,
                    GenralOperator.PLUS.value,
                    GenralOperator.MINUS,
                ).any {
                    it == text
                } || GeneralKey.Equal.value == text
            val fontSize =
                if (operator) {
                    32.sp
                } else {
                    26.sp
                }
            Text(
                text = text,
                style = TextSet.bold.copy(pressColor, fontSize),
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
