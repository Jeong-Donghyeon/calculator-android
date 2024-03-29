package com.donghyeon.dev.calculator.percent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.R
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
    val context = LocalContext.current
    BackHandler { main.navigation(Nav.POP, null) }
    LaunchedEffect(Unit) {
        v1Focus.requestFocus()
        viewModel.sideEffect.collectLatest {
            when (it) {
                is SideEffect.Toast -> main.showToast(context.getString(it.id))
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
    val guideStrArr = stringArrayResource(id = R.array.percent_guide)
    val calculate = state.getCalculate()
    val selectColor: (Boolean) -> Color = {
        if (it) ColorSet.select else ColorSet.text
    }
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ViewTitle(
            title = stringResource(id = Dest.PERCENT.title),
            navDest = { navDest?.invoke(it) },
        )
        Spacer(modifier = Modifier.weight(1f))
        PercentValue.entries.forEach { value ->
            val color = selectColor(calculate.select == value)
            val focus = if (value == PercentValue.VALUE1) v1Focus else v2Focus
            Row(
                modifier = Modifier.padding(end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(end = 10.dp, top = 5.dp),
                    text = value.value,
                    style = TextSet.extraBold.copy(color, 24.sp),
                    textAlign = TextAlign.Center,
                )
                ViewFieldNumber(
                    modifier =
                        Modifier
                            .width(220.dp)
                            .focusRequester(focus ?: FocusRequester())
                            .onFocusChanged {
                                if (it.isFocused) {
                                    action?.inputKey(
                                        if (value == PercentValue.VALUE1) {
                                            PercentKey.Value1
                                        } else {
                                            PercentKey.Value2
                                        },
                                    )
                                }
                            },
                    value = calculate.getValue(value),
                    color = color,
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Spacer(modifier = Modifier.weight(2f))
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
        Spacer(modifier = Modifier.height(5.dp))
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text =
                state.type?.ordinal?.let {
                    guideStrArr[it]
                } ?: "",
            style = TextSet.bold.copy(ColorSet.text, 18.sp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.weight(1f))
        state.type?.ordinal?.let {
            ViewScrollTab(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 3.dp),
                tabs = stringArrayResource(id = R.array.percent_type).toList(),
                index = it,
                onTab = { action?.inputType(it) },
            )
        } ?: Spacer(modifier = Modifier.height(55.dp))
        KeyView(
            state = state,
            action = action,
            v1Focus = v1Focus,
            v2Focus = v2Focus,
        )
    }
}

@Composable
private fun KeyView(
    state: PercentState,
    action: PercentAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    val keyList1 =
        listOf(
            PercentKey.Clear,
            PercentKey.Left,
            PercentKey.Right,
            PercentKey.Backspace,
        )
    val keyList2 =
        listOf(
            listOf(
                PercentKey.Seven,
                PercentKey.Eight,
                PercentKey.Nine,
            ),
            listOf(
                PercentKey.Four,
                PercentKey.Five,
                PercentKey.Six,
            ),
            listOf(
                PercentKey.One,
                PercentKey.Two,
                PercentKey.Three,
            ),
            listOf(
                PercentKey.ZeroZero,
                PercentKey.Zero,
                PercentKey.Decimal,
            ),
        )
    val keyList3 =
        listOf(
            PercentKey.Value1,
            PercentKey.Value2,
        )
    val viewButtonKey: @Composable RowScope.(PercentKey) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .height(InputKeyHeight.value.dp),
            text = it.value,
            icon =
                when (it) {
                    is PercentKey.Left -> it.value.toInt() to 32.dp
                    is PercentKey.Right -> it.value.toInt() to 32.dp
                    is PercentKey.Backspace -> it.value.toInt() to 32.dp
                    else -> null
                },
            onClick = { action?.inputKey(it) },
        )
    }
    Column(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 3.dp),
    ) {
        Row {
            keyList1.forEach {
                viewButtonKey(it)
            }
        }
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(modifier = Modifier.weight(3f)) {
                keyList2.forEach {
                    Row {
                        it.forEach {
                            viewButtonKey(it)
                        }
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                keyList3.forEach {
                    val color =
                        if (it.value == state.getCalculate().select.value) {
                            ColorSet.select
                        } else {
                            ColorSet.text
                        }
                    ViewButtonKeyValue(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .weight(1f),
                        text = it.value,
                        color = color,
                        onClick = {
                            when (it) {
                                is PercentKey.Value1 -> v1Focus?.requestFocus()
                                is PercentKey.Value2 -> v2Focus?.requestFocus()
                                else -> {}
                            }
                        },
                    )
                }
            }
        }
    }
}
