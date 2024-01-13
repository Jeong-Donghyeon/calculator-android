package dev.donghyeon.calculator.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_TitleView() {
    TitleView(
        title = Destination.General.route,
    )
}

@Composable
fun TitleView(title: String) {
    Box(
        modifier =
            Modifier
                .background(ColorSet.container)
                .fillMaxWidth()
                .height(50.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
        )
    }
}
