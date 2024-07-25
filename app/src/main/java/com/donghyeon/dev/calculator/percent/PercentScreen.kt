package com.donghyeon.dev.calculator.percent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.MainAction
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.PercentType
import com.donghyeon.dev.calculator.calculate.PercentUnit
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.FontSizeRange
import com.donghyeon.dev.calculator.view.Keyboard
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewKeyboard
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.donghyeon.dev.calculator.view.ViewTextResult
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_PercentScreen() {
    PercentScreen(
        state =
            PercentState(
                type = PercentType.RATIO_VALUE,
            ),
    )
}

@Composable
fun PercentScreen(
    state: PercentState,
    action: PercentAction? = null,
    mainAction: MainAction? = null,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    val guideStrArr = stringArrayResource(id = R.array.percent_guide)
    val focus = remember { FocusRequester() }
    val calculate = state.getCalculate()
    val selectColor: (Boolean) -> Color = {
        if (it) ColorSet.select else ColorSet.text
    }
    LaunchedEffect(Unit) {
        focus.requestFocus()
        action?.sideEffect?.collectLatest {
            when (it) {
                is SideEffect.Toast ->
                    mainAction?.showToast(context.getString(it.id))
                else -> {}
            }
        }
    }
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.type?.let { type ->
            Spacer(modifier = Modifier.weight(1f))
            ViewFieldNumber(
                modifier =
                    Modifier
                        .width(220.dp)
                        .focusRequester(focus)
                        .onFocusChanged {
                            if (it.isFocused) action?.inputV1Focus()
                        },
                value = calculate.valueList[0],
                color = selectColor(state.v1Focus),
            )
            ViewFieldNumber(
                modifier =
                    Modifier
                        .width(220.dp)
                        .onFocusChanged {
                            if (it.isFocused) action?.inputV2Focus()
                        },
                value = calculate.valueList[1],
                color = selectColor(state.v2Focus),
            )
            Spacer(modifier = Modifier.height(20.dp))
            ViewTextResult(
                modifier = Modifier.width(300.dp),
                text =
                    calculate.result
                        .replace(PercentUnit.UP.value, stringResource(id = R.string.up))
                        .replace(PercentUnit.DOWN.value, stringResource(id = R.string.down)),
                fontSizeRange =
                    FontSizeRange(
                        min = 1.sp,
                        max = 30.sp,
                    ),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = guideStrArr[type.ordinal],
                style = TextSet.bold.copy(ColorSet.text, 18.sp),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Spacer(modifier = Modifier.weight(1.3f))
            ViewScrollTab(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 3.dp),
                tabs = stringArrayResource(id = R.array.percent_type).toList(),
                index = type.ordinal,
                onTab = {
                    focusManager.clearFocus()
                    focus.requestFocus()
                    action?.inputType(it)
                },
            )
        } ?: Spacer(modifier = Modifier.weight(1f))
        ViewKeyboard {
            when (it) {
                is Keyboard.Enter -> focusManager.moveFocus(FocusDirection.Next)
                is Keyboard.Copy -> {
                    val copyStr =
                        calculate.result
                            .replace(",", "")
                            .replace(PercentUnit.PERCENT.value, "")
                    if (copyStr != "" && copyStr != "?") {
                        clipboardManager.setText(AnnotatedString(copyStr))
                    }
                }
                is Keyboard.Paste ->
                    action?.inputKey(
                        Keyboard.Paste(clipboardManager.getText().toString()),
                    )
                else -> action?.inputKey(it)
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
    }
}
