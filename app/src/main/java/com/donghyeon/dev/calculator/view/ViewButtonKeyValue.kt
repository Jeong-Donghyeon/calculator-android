package com.donghyeon.dev.calculator.view

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.percent.PercentValue
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewButtonKeyValue() {
    ViewButtonKeyValue(
        text = PercentValue.VALUE1.value,
        color = ColorSet.select,
    )
}

@Composable
fun ViewButtonKeyValue(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: (() -> Unit)? = null,
) {
    val vibrator = LocalContext.current.getSystemService(Vibrator::class.java)
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(0.dp, ColorSet.transparent),
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
            onClick?.invoke()
        },
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
            style = TextSet.bold.copy(color, 24.sp),
        )
    }
}
