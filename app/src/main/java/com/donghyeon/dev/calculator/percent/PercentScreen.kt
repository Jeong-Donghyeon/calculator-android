package com.donghyeon.dev.calculator.percent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.PercentCalculateType
import com.donghyeon.dev.calculator.calculate.PercentUnit
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.FontSizeRange
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewButtonKeyValue
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.donghyeon.dev.calculator.view.ViewTextResult
import com.donghyeon.dev.calculator.view.ViewTitle
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_PercentScreen() {
    PercentScreen(
        state = PercentState(),
    )
}

@Composable
fun PercentScreen() {
    val viewModel: PercentViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    val v1Focus = remember { FocusRequester() }
    val v2Focus = remember { FocusRequester() }
    BackHandler { main.navigation(Nav.POP, null) }
    LaunchedEffect(Unit) {
        v1Focus.requestFocus()
        viewModel.sideEffect.collectLatest {
            when (it) {
                is SideEffect.Toast -> main.showToast(it.message)
                is SideEffect.Focus ->
                    when (it.fieldName) {
                        PercentKey.Value1.value -> v1Focus.requestFocus()
                        PercentKey.Value2.value -> v2Focus.requestFocus()
                    }
            }
        }
    }
    PercentScreen(
        state = state,
        action = viewModel,
        navDest = { main.navigation(Nav.PUSH, it) },
        v1Focus = v1Focus,
        v2Focus = v2Focus,
    )
}

@Composable
private fun PercentScreen(
    state: PercentState,
    action: PercentAction? = null,
    navDest: ((Dest) -> Unit)? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    Column(modifier = Modifier.background(ColorSet.background)) {
        ViewTitle(
            title = stringResource(id = Dest.PERCENT.title),
            navDest = { navDest?.invoke(it) },
        )
        Box(modifier = Modifier.weight(1f)) {
            CalculateView(
                state = state,
                action = action,
                v1Focus = v1Focus,
                v2Focus = v2Focus,
            )
        }
        ViewScrollTab(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 7.dp),
            tabs = stringArrayResource(id = R.array.percent_type).toList(),
            index = state.type.ordinal,
            onTab = {
                when (it) {
                    0 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE1)
                    1 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE2)
                    2 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE3)
                    3 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE4)
                }
            },
        )
        KeyView(
            state = state,
            action = action,
            v1Focus = v1Focus,
            v2Focus = v2Focus,
        )
    }
}

@Composable
private fun CalculateView(
    state: PercentState,
    action: PercentAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    val guideStrArr = stringArrayResource(id = R.array.percent_guide)
    val (calculate, guideStr) =
        when (state.type) {
            PercentCalculateType.TYPE1 -> state.calculate1 to guideStrArr[0]
            PercentCalculateType.TYPE2 -> state.calculate2 to guideStrArr[1]
            PercentCalculateType.TYPE3 -> state.calculate3 to guideStrArr[2]
            PercentCalculateType.TYPE4 -> state.calculate4 to guideStrArr[3]
        }
    val (v1Color, v2Color) =
        when (calculate.select) {
            PercentValueSelect.VALUE1 -> ColorSet.select to ColorSet.text
            PercentValueSelect.VALUE2 -> ColorSet.text to ColorSet.select
        }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = PercentKey.Value1.value,
                style = TextSet.extraBold.copy(v1Color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewFieldNumber(
                modifier =
                    Modifier
                        .width(220.dp)
                        .focusRequester(v1Focus ?: FocusRequester())
                        .onFocusChanged {
                            if (it.isFocused) {
                                action?.inputPercentValueSelect(PercentValueSelect.VALUE1)
                            }
                        },
                value = calculate.value1,
                color = v1Color,
            )
        }
        Row(
            modifier = Modifier.padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = "V2",
                style = TextSet.extraBold.copy(v2Color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewFieldNumber(
                modifier =
                    Modifier
                        .width(220.dp)
                        .focusRequester(v2Focus ?: FocusRequester())
                        .onFocusChanged {
                            if (it.isFocused) {
                                action?.inputPercentValueSelect(PercentValueSelect.VALUE2)
                            }
                        },
                value = calculate.value2,
                color = v2Color,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                ViewTextResult(
                    modifier = Modifier.width(300.dp),
                    text =
                        calculate.result
                            .replace(PercentUnit.UP.value, stringResource(id = R.string.up))
                            .replace(PercentUnit.DOWN.value, stringResource(id = R.string.down)),
                    fontSizeRange =
                        FontSizeRange(
                            min = 1.sp,
                            max = 34.sp,
                        ),
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = guideStr,
                style = TextSet.bold.copy(ColorSet.text, 18.sp),
            )
        }
    }
}

@Composable
private fun KeyView(
    state: PercentState,
    action: PercentAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val keyList =
        listOf(
            listOf(
                PercentKey.Clear,
                PercentKey.Seven,
                PercentKey.Four,
                PercentKey.One,
                PercentKey.Copy,
            ),
            listOf(
                PercentKey.Left,
                PercentKey.Eight,
                PercentKey.Five,
                PercentKey.Two,
                PercentKey.Zero,
            ),
            listOf(
                PercentKey.Right,
                PercentKey.Nine,
                PercentKey.Six,
                PercentKey.Three,
                PercentKey.Decimal,
            ),
            listOf(
                PercentKey.Backspace,
                PercentKey.Value1,
                PercentKey.Value2,
            ),
        )
    val height = keyList.first().count() * InputKeyHeight.value
    val calculate =
        when (state.type) {
            PercentCalculateType.TYPE1 -> state.calculate1
            PercentCalculateType.TYPE2 -> state.calculate2
            PercentCalculateType.TYPE3 -> state.calculate3
            PercentCalculateType.TYPE4 -> state.calculate4
        }
    Row(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .height(height.dp),
    ) {
        keyList.forEach { row ->
            Column(modifier = Modifier.weight(1f)) {
                row.forEach { key ->
                    when (key) {
                        is PercentKey.Value1, PercentKey.Value2 -> {
                            val color =
                                if (key.value == calculate.select.value) {
                                    ColorSet.select
                                } else {
                                    ColorSet.text
                                }
                            ViewButtonKeyValue(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(2f)
                                        .padding(2.dp),
                                text = key.value,
                                color = color,
                                onClick = {
                                    when (key) {
                                        is PercentKey.Value1 -> v1Focus?.requestFocus()
                                        is PercentKey.Value2 -> v2Focus?.requestFocus()
                                        else -> {}
                                    }
                                },
                            )
                        }
                        else ->
                            ViewButtonKey(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(2.dp),
                                text = key.value,
                                icon =
                                    when (key) {
                                        is PercentKey.Left -> key.value.toInt() to 32.dp
                                        is PercentKey.Right -> key.value.toInt() to 32.dp
                                        is PercentKey.Copy -> key.value.toInt() to 26.dp
                                        is PercentKey.Backspace -> key.value.toInt() to 32.dp
                                        else -> null
                                    },
                                onClick = {
                                    if (key is PercentKey.Copy) {
                                        val copyStr =
                                            calculate.result
                                                .replace(",", "")
                                                .replace(PercentUnit.PERCENT.value, "")
                                                .replace(PercentUnit.UP.value, "")
                                                .replace(PercentUnit.DOWN.value, "")
                                                .trim()
                                        clipboardManager.setText(
                                            AnnotatedString(copyStr),
                                        )
                                    }
                                    action?.inputKey(key)
                                },
                            )
                    }
                }
            }
        }
    }
}
