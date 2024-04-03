package com.donghyeon.dev.calculator.convert

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.donghyeon.dev.calculator.view.ViewTitle
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_ConvertScreen() {
    ConvertScreen(
        state = ConvertState(),
    )
}

@Composable
fun ConvertScreen() {
    val viewModel: ConvertViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    val v1Focus = remember { FocusRequester() }
    val v2Focus = remember { FocusRequester() }
    val context = LocalContext.current
    BackHandler { main.navigation(Nav.POP, null) }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest {
            when (it) {
                is SideEffect.Toast -> main.showToast(context.getString(it.id))
                is SideEffect.Focus ->
                    when (it.fieldName) {
//                        ConvertKey.Value1.value -> v1Focus.requestFocus()
//                        ConvertKey.Value2.value -> v2Focus.requestFocus()
                    }
            }
        }
    }
    ConvertScreen(
        state = state,
        action = viewModel,
        navDest = { main.navigation(Nav.PUSH, it) },
        v1Focus = v1Focus,
        v2Focus = v2Focus,
    )
}

@Composable
private fun ConvertScreen(
    state: ConvertState,
    action: ConvertAction? = null,
    navDest: ((Dest) -> Unit)? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ViewTitle(
            title = stringResource(id = Dest.CONVERT.title),
            navDest = { navDest?.invoke(it) },
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(20.dp))
        UnitInputView(
            u = "U1",
            unit = "km",
            v = "V1",
            value = TextFieldValue(""),
            select = true,
        )
        Spacer(modifier = Modifier.height(20.dp))
        UnitInputView(
            u = "U2",
            unit = "m",
            v = "V2",
            value = TextFieldValue(""),
            select = false,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.weight(1f))
        ViewScrollTab(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 3.dp),
            tabs = stringArrayResource(id = R.array.convert_type).toList(),
            index = state.type.ordinal,
            onTab = { action?.inputType(it) },
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
private fun UnitInputView(
    u: String,
    unit: String,
    v: String,
    value: TextFieldValue,
    select: Boolean,
) {
    val color =
        if (select) {
            ColorSet.select
        } else {
            ColorSet.text
        }
    Column(modifier = Modifier.width(300.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.width(50.dp),
                text = u,
                style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
            )
            ViewButtonKey(text = unit)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(end = 10.dp, top = 5.dp),
                text = v,
                style = TextSet.extraBold.copy(color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewFieldNumber(
                modifier = Modifier.weight(1f),
                value = value,
                color = color,
            )
            Text(
                modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                text = unit,
                style = TextSet.extraBold.copy(color, 24.sp),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun KeyView(
    state: ConvertState,
    action: ConvertAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    val keyList =
        listOf(
            listOf(
                ConvertKey.Clear,
                ConvertKey.Left,
                ConvertKey.Right,
                ConvertKey.Backspace,
            ),
            listOf(
                ConvertKey.Seven,
                ConvertKey.Eight,
                ConvertKey.Nine,
                ConvertKey.Unit1,
            ),
            listOf(
                ConvertKey.Four,
                ConvertKey.Five,
                ConvertKey.Six,
                ConvertKey.Value1,
            ),
            listOf(
                ConvertKey.One,
                ConvertKey.Two,
                ConvertKey.Three,
                ConvertKey.Unit2,
            ),
            listOf(
                ConvertKey.ZeroZero,
                ConvertKey.Zero,
                ConvertKey.Decimal,
                ConvertKey.Value2,
            ),
        )
    Column(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 3.dp),
    ) {
        keyList.forEach {
            Row {
                it.forEach {
                    ViewButtonKey(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .weight(1f)
                                .height(InputKeyHeight),
                        text = it.value,
                        icon =
                            when (it) {
                                is ConvertKey.Left -> it.value.toInt() to 32.dp
                                is ConvertKey.Right -> it.value.toInt() to 32.dp
                                is ConvertKey.Backspace -> it.value.toInt() to 32.dp
                                else -> null
                            },
                    )
                }
            }
        }
    }
}
