package dev.donghyeon.calculator.info

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
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
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
    BackHandler { main.navigation(Navigation.Pop) }
    InfoScreen(
        appName = stringResource(id = R.string.app_name),
        version = BuildConfig.VERSION_NAME,
        navBack = { main.navigation(Navigation.Pop) },
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
                .background(ColorSet.container)
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                    painter = painterResource(id = R.drawable.ic_back_24px),
                    tint = ColorSet.text,
                    contentDescription = "Back",
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "정보",
                style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
        Image(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(ColorSet.button)
                    .size(120.dp),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "AppIcon",
        )
        Spacer(modifier = Modifier.height(26.dp))
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
            text = "개인정보 처리방침",
            style = TextSet.regular.copy(linkColor, 18.sp),
            textDecoration = TextDecoration.Underline,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier.clickable { license?.invoke() },
            text = "오픈소스 라이선스",
            style = TextSet.regular.copy(linkColor, 18.sp),
            textDecoration = TextDecoration.Underline,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier.clickable { terms?.invoke() },
            text = "면책 조항",
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
                text =
                    """
                    본 앱을 사용함으로써 귀하는
                    아래의 이용약관에 동의하게 됩니다.

                    앱을 통해 제공되는 모든 계산 결과 값이나
                    정보들의 정확성, 신뢰성 또는 적절성에 대해
                    어떠한 보증도 하지 않습니다.

                    제공되는 정보들에 의하여 발생 할 수 있는
                    직접적, 간접적인 모든 손해에 대하여
                    책임을 지지 않습니다.
                    
                    또한 지속적인 업데이트에 대한 책임이 없습니다.
                    """.trimIndent(),
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
