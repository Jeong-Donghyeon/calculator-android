package com.donghyeon.dev.calculator.info

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.donghyeon.dev.calculator.BuildConfig
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Preview
@Composable
private fun Preview_InfoScreen() {
    InfoScreen(
        appName = "계산기",
        version = "1.0.0",
    )
}

@Preview
@Composable
private fun Preview_TermsAlert() {
    TermsAlert()
}

@Composable
fun InfoScreen() {
    val context = LocalContext.current
    val main = LocalViewModel.current
    var termsAlet by remember { mutableStateOf(false) }
    BackHandler { main.navigation(Nav.POP, null) }
    InfoScreen(
        appName = stringResource(id = R.string.app_name),
        version = BuildConfig.VERSION_NAME,
        navBack = { main.navigation(Nav.POP, null) },
        license = {
            context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        },
        terms = { termsAlet = true },
    )
    if (termsAlet) {
        TermsAlert(close = { termsAlet = false })
    }
}

@Composable
private fun InfoScreen(
    appName: String,
    version: String,
    navBack: (() -> Unit)? = null,
    license: (() -> Unit)? = null,
    terms: (() -> Unit)? = null,
) {
    val linkColor = ColorSet.text.copy(alpha = 0.7f)
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .background(ColorSet.background)
                    .fillMaxWidth()
                    .height(50.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(
                onClick = { navBack?.invoke() },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_back_24px),
                    tint = ColorSet.text,
                    contentDescription = "Back",
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = Dest.INFO.title),
                style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(ColorSet.button)
                        .size(90.dp),
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "AppIcon",
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = appName,
            style = TextSet.extraBold.copy(ColorSet.result, 26.sp),
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "ver $version",
            style = TextSet.bold.copy(ColorSet.text, 20.sp),
        )
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = stringResource(id = R.string.privacy_policy),
            style = TextSet.regular.copy(linkColor, 18.sp),
            textDecoration = TextDecoration.Underline,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier.clickable { license?.invoke() },
            text = stringResource(id = R.string.open_source_license),
            style = TextSet.regular.copy(linkColor, 18.sp),
            textDecoration = TextDecoration.Underline,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier.clickable { terms?.invoke() },
            text = stringResource(id = R.string.disclaimer),
            style = TextSet.regular.copy(linkColor, 18.sp),
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
private fun TermsAlert(close: (() -> Unit)? = null) {
    Dialog(
        onDismissRequest = close ?: {},
        properties = DialogProperties(),
    ) {
        Column(modifier = Modifier.background(ColorSet.button)) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = R.string.disclaimer_content).trimIndent(),
                style = TextSet.regular.copy(ColorSet.text, 14.sp),
            )
            Box(
                modifier =
                    Modifier
                        .padding(horizontal = 7.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorSet.text.copy(alpha = 0.1f)),
            )
            Box(
                modifier =
                    Modifier
                        .clickable { close?.invoke() }
                        .fillMaxWidth()
                        .height(45.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "확인",
                    style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
