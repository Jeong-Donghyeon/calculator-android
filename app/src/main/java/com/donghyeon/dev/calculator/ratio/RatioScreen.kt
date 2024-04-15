package com.donghyeon.dev.calculator.ratio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.MainAction
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.Keyboard
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewKeyboard
import com.donghyeon.dev.calculator.view.ViewScrollTab
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_RatioScreen_Null() =
    RatioScreen(
        state =
            RatioState(
                type = null,
            ),
    )

@Preview
@Composable
private fun Preview_RatioScreen_Ratio() =
    RatioScreen(
        state =
            RatioState(
                type = RatioType.RATIO,
            ),
    )

@Preview
@Composable
private fun Preview_RatioScreen_Simplify() =
    RatioScreen(
        state =
            RatioState(
                type = RatioType.SIMPLIFY,
            ),
    )

@Composable
fun RatioScreen(
    state: RatioState,
    action: RatioAction? = null,
    mainAction: MainAction? = null,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    val focus = remember { FocusRequester() }
    val calculate = state.getCalculate()
    val selectColor: (Boolean) -> Color = {
        if (it) ColorSet.select else ColorSet.text
    }
    LaunchedEffect(Unit) {
        focus.requestFocus()
        action?.sideEffect?.collectLatest {
            when (it) {
                is SideEffect.Toast -> mainAction?.showToast(context.getString(it.id))
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
            val modifier = Modifier.width(300.dp).height(90.dp)
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ViewFieldNumber(
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(focus)
                            .onFocusChanged {
                                if (it.isFocused) action?.inputV1Focus()
                            },
                    value = state.getCalculate().valueList[0],
                    color = selectColor(state.v1Focus),
                    align = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.width(30.dp),
                    text = ":",
                    style = TextSet.bold.copy(ColorSet.text, 20.sp),
                    textAlign = TextAlign.Center,
                )
                ViewFieldNumber(
                    modifier =
                        Modifier
                            .weight(1f)
                            .onFocusChanged {
                                if (it.isFocused) action?.inputV2Focus()
                            },
                    value = state.getCalculate().valueList[1],
                    color = selectColor(state.v2Focus),
                    align = TextAlign.Center,
                )
            }
            when (type) {
                RatioType.RATIO -> {
                    Row(
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ViewFieldNumber(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .onFocusChanged {
                                        if (it.isFocused) action?.inputV3Focus()
                                    },
                            value = state.getCalculate().valueList[2],
                            color = selectColor(state.v3Focus),
                            align = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.width(30.dp),
                            text = ":",
                            style = TextSet.bold.copy(ColorSet.text, 20.sp),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = calculate.result,
                            style = TextSet.bold.copy(ColorSet.result, 28.sp),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                RatioType.SIMPLIFY -> {
                    val result = calculate.result.split(":")
                    val (result1, result2) =
                        if (result.count() == 2) {
                            result[0] to result[1]
                        } else {
                            "?" to "?"
                        }
                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = result1,
                            style = TextSet.bold.copy(ColorSet.result, 28.sp),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.width(30.dp),
                            text = ":",
                            style = TextSet.bold.copy(ColorSet.result, 28.sp),
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = result2,
                            style = TextSet.bold.copy(ColorSet.result, 28.sp),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ViewScrollTab(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 3.dp),
                tabs = stringArrayResource(id = R.array.ratio_type).toList(),
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
                    state.type?.let { type ->
                        val copyStr =
                            when (type) {
                                RatioType.RATIO ->
                                    calculate.result.replace(",", "")
                                RatioType.SIMPLIFY ->
                                    calculate.result.split(":")[state.getValueIndex()]
                                        .replace(",", "")
                            }
                        if (copyStr != "" && copyStr != "?") {
                            clipboardManager.setText(AnnotatedString(copyStr))
                        }
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
