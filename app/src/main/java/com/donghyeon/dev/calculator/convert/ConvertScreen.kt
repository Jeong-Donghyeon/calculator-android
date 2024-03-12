package com.donghyeon.dev.calculator.convert

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewButtonKeyValue
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
                        ConvertKey.Value1.value -> v1Focus.requestFocus()
                        ConvertKey.Value2.value -> v2Focus.requestFocus()
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
            title = stringResource(id = Dest.PERCENT.title),
            navDest = { navDest?.invoke(it) },
        )
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
private fun KeyView(
    state: ConvertState,
    action: ConvertAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val keyList =
        listOf(
            listOf(
                ConvertKey.Clear,
                ConvertKey.Seven,
                ConvertKey.Four,
                ConvertKey.One,
                ConvertKey.ZeroZero,
            ),
            listOf(
                ConvertKey.Left,
                ConvertKey.Eight,
                ConvertKey.Five,
                ConvertKey.Two,
                ConvertKey.Zero,
            ),
            listOf(
                ConvertKey.Right,
                ConvertKey.Nine,
                ConvertKey.Six,
                ConvertKey.Three,
                ConvertKey.Decimal,
            ),
            listOf(
                ConvertKey.Backspace,
                ConvertKey.Value1,
                ConvertKey.Value2,
            ),
        )
    val height = keyList.first().count() * InputKeyHeight.value
    val calculate = state.getCalculate()
    Row(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 3.dp)
                .height(height.dp),
    ) {
        keyList.forEach { row ->
            Column(modifier = Modifier.weight(1f)) {
                row.forEach { key ->
                    when (key) {
                        is ConvertKey.Value1, ConvertKey.Value2 -> {
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
                                        is ConvertKey.Value1 -> v1Focus?.requestFocus()
                                        is ConvertKey.Value2 -> v2Focus?.requestFocus()
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
                                        is ConvertKey.Left -> key.value.toInt() to 32.dp
                                        is ConvertKey.Right -> key.value.toInt() to 32.dp
                                        is ConvertKey.Backspace -> key.value.toInt() to 32.dp
                                        else -> null
                                    },
                                onClick = { action?.inputKey(key) },
                            )
                    }
                }
            }
        }
    }
}
