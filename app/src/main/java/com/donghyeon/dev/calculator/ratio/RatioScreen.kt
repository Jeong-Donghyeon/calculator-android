package com.donghyeon.dev.calculator.ratio

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewButtonKeyValue
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewScrollTab
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_RatioScreen() {
    RatioScreen(
        state = RatioState(),
    )
}

@Composable
fun RatioScreen() {
    val viewModel: RatioViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    val v1Focus = remember { FocusRequester() }
    val v2Focus = remember { FocusRequester() }
    val v3Focus = remember { FocusRequester() }
    val context = LocalContext.current
    BackHandler { main.navigation(Nav.POP, null) }
    LaunchedEffect(Unit) {
        v1Focus.requestFocus()
        viewModel.sideEffect.collectLatest {
            when (it) {
                is SideEffect.Toast -> main.showToast(context.getString(it.id))
                is SideEffect.Focus ->
                    when (it.fieldName) {
                        RatioKey.Value1.value -> v1Focus.requestFocus()
                        RatioKey.Value2.value -> v2Focus.requestFocus()
                        RatioKey.Value3.value -> v3Focus.requestFocus()
                    }
            }
        }
    }
    RatioScreen(
        state = state,
        action = viewModel,
        v1Focus = v1Focus,
        v2Focus = v2Focus,
        v3Focus = v3Focus,
    )
}

@Composable
fun RatioScreen(
    state: RatioState,
    action: RatioAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
    v3Focus: FocusRequester? = null,
) {
    val calculate = state.getCalculate()
    val selectColor: (Boolean) -> Color = {
        if (it) ColorSet.select else ColorSet.text
    }
    val v1Color = selectColor(calculate.select == RatioValue.VALUE1)
    val v2Color = selectColor(calculate.select == RatioValue.VALUE2)
    val v3Color = selectColor(calculate.select == RatioValue.VALUE3)
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.width(300.dp)) {
            Text(
                modifier = Modifier.weight(1f),
                text = RatioKey.Value1.value,
                style = TextSet.bold.copy(v1Color, 20.sp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = RatioKey.Value2.value,
                style = TextSet.bold.copy(v2Color, 20.sp),
                textAlign = TextAlign.Center,
            )
        }
        Row(
            modifier = Modifier.width(300.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ViewFieldNumber(
                modifier =
                    Modifier
                        .weight(1f)
                        .focusRequester(v1Focus ?: FocusRequester())
                        .onFocusChanged {
                            if (it.isFocused) {
                                action?.inputKey(RatioKey.Value1)
                            }
                        },
                value = state.getCalculate().valueList[0],
                color = v1Color,
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
                        .focusRequester(v2Focus ?: FocusRequester())
                        .onFocusChanged {
                            if (it.isFocused) {
                                action?.inputKey(RatioKey.Value2)
                            }
                        },
                value = state.getCalculate().valueList[1],
                color = v2Color,
                align = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (state.type == RatioType.RATIO) {
            Row(modifier = Modifier.width(300.dp)) {
                Text(
                    modifier = Modifier.width(135.dp),
                    text = RatioKey.Value3.value,
                    style = TextSet.bold.copy(v3Color, 20.sp),
                    textAlign = TextAlign.Center,
                )
            }
            Row(
                modifier = Modifier.width(300.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ViewFieldNumber(
                    modifier =
                        Modifier
                            .weight(1f)
                            .focusRequester(v3Focus ?: FocusRequester())
                            .onFocusChanged {
                                if (it.isFocused) {
                                    action?.inputKey(RatioKey.Value3)
                                }
                            },
                    value = state.getCalculate().valueList[2],
                    color = v3Color,
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
                    style = TextSet.bold.copy(ColorSet.result, 26.sp),
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            val result = calculate.result.split(":")
            val (result1, result2) =
                if (result.count() == 2) {
                    result[0] to result[1]
                } else {
                    "?" to "?"
                }
            Row(
                modifier =
                    Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(end = 16.dp).weight(1f),
                    text = result1,
                    style = TextSet.bold.copy(ColorSet.result, 28.sp),
                    textAlign = TextAlign.Right,
                )
                Text(
                    text = ":",
                    style = TextSet.bold.copy(ColorSet.result, 28.sp),
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp).weight(1f),
                    text = result2,
                    style = TextSet.bold.copy(ColorSet.result, 28.sp),
                    textAlign = TextAlign.Left,
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.weight(1f))
        ViewScrollTab(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 3.dp),
            tabs = stringArrayResource(id = R.array.ratio_type).toList(),
            index = state.type.index,
            onTab = { action?.inputType(it) },
        )
        KeyView(
            state = state,
            action = action,
            v1Focus = v1Focus,
            v2Focus = v2Focus,
            v3Focus = v3Focus,
        )
    }
}

@Composable
private fun KeyView(
    state: RatioState,
    action: RatioAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
    v3Focus: FocusRequester? = null,
) {
    val keyList1 =
        listOf(
            RatioKey.Clear,
            RatioKey.Left,
            RatioKey.Right,
            RatioKey.Backspace,
        )
    val keyList2 =
        listOf(
            listOf(
                RatioKey.Seven,
                RatioKey.Eight,
                RatioKey.Nine,
            ),
            listOf(
                RatioKey.Four,
                RatioKey.Five,
                RatioKey.Six,
            ),
            listOf(
                RatioKey.One,
                RatioKey.Two,
                RatioKey.Three,
            ),
            listOf(
                RatioKey.ZeroZero,
                RatioKey.Zero,
                RatioKey.Decimal,
            ),
        )
    val keyList3 =
        if (state.type == RatioType.RATIO) {
            listOf(
                RatioKey.Value1,
                RatioKey.Value2,
                RatioKey.Value3,
            )
        } else {
            listOf(
                RatioKey.Value1,
                RatioKey.Value2,
            )
        }
    val viewButtonKey: @Composable RowScope.(RatioKey) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .height(InputKeyHeight.value.dp),
            text = it.value,
            icon =
                when (it) {
                    is RatioKey.Left -> it.value.toInt() to 32.dp
                    is RatioKey.Right -> it.value.toInt() to 32.dp
                    is RatioKey.Backspace -> it.value.toInt() to 32.dp
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
                                is RatioKey.Value1 -> v1Focus?.requestFocus()
                                is RatioKey.Value2 -> v2Focus?.requestFocus()
                                is RatioKey.Value3 -> v3Focus?.requestFocus()
                                else -> {}
                            }
                        },
                    )
                }
            }
        }
    }
}
