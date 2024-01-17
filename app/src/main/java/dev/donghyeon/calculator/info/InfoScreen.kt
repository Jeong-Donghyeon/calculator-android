package dev.donghyeon.calculator.info

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.donghyeon.calculator.BuildConfig
import dev.donghyeon.calculator.Navigation
import dev.donghyeon.calculator.R
import dev.donghyeon.calculator.common.LocalViewModel
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
private fun Preview_InfoScreen() {
    InfoScreen(
        appName = "계산기",
        version = "1.0.0",
    )
}

@Composable
fun InfoScreen() {
    val main = LocalViewModel.current
    BackHandler { main.navigation(Navigation.Pop) }
    InfoScreen(
        appName = stringResource(id = R.string.app_name),
        version = BuildConfig.VERSION_NAME,
        navBack = { main.navigation(Navigation.Pop) },
    )
}

@Composable
fun InfoScreen(
    appName: String,
    version: String,
    navBack: (() -> Unit)? = null,
) {
    Column(
        modifier =
            Modifier
                .background(ColorSet.container)
                .fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier
                    .background(ColorSet.container)
                    .fillMaxWidth()
                    .height(50.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(
                onClick = { navBack?.invoke() },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_back),
                    tint = ColorSet.text,
                    contentDescription = "info",
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "정보",
                style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
                textAlign = TextAlign.Center,
            )
        }
    }
}
