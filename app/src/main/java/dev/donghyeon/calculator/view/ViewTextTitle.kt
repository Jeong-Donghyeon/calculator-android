package dev.donghyeon.calculator.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.R
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_TitleView() {
    TitleView(
        title = Destination.GENERAL.route,
    )
}

@Composable
fun TitleView(
    title: String,
    navInfo: ((Destination) -> Unit)? = null,
) {
    Box(
        modifier =
            Modifier
                .background(ColorSet.container)
                .fillMaxWidth()
                .height(50.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
            textAlign = TextAlign.Center,
        )
        IconButton(
            onClick = { navInfo?.invoke(Destination.INTRO) },
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_info),
                tint = ColorSet.text,
                contentDescription = "info",
            )
        }
    }
}
