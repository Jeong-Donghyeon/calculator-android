package com.donghyeon.dev.calculator.view

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Menu
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewTitle() {
    ViewTitle(
        title = stringResource(id = Menu.GENERAL.title),
    )
}

@Composable
fun ViewTitle(
    title: String,
    navDest: ((Dest) -> Unit)? = null,
) {
    Box(
        modifier =
            Modifier
                .background(ColorSet.background)
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
            onClick = { navDest?.invoke(Dest.INFO) },
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_info_24px),
                tint = ColorSet.text,
                contentDescription = "Info",
            )
        }
    }
}
