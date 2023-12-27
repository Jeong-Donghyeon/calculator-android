package dev.donghyeon.calculator.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.theme.DarkGray
import dev.donghyeon.calculator.theme.TsBold
import dev.donghyeon.calculator.theme.White

@Preview
@Composable
fun Preview_BottonView() = BottonView(text = "0", onClick = {})

@Composable
fun BottonView(
    modifier: Modifier = Modifier,
    text: String,
    height: Dp = 70.dp,
    background: Color = DarkGray,
    textColor: Color = White,
    shape: Shape = RoundedCornerShape(5.dp),
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth().height(height).then(modifier),
        shape = shape,
        border = border,
        onClick = onClick,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            contentColor = textColor
        ),
        elevation = null
    ) {
        Text(
            text = text,
            style = TsBold.copy(textColor, 24.sp)
        )
    }
}
