package com.donghyeon.dev.calculator.general

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.view.FontSizeRange
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewFieldGeneral
import com.donghyeon.dev.calculator.view.ViewTextResult
import com.donghyeon.dev.calculator.view.ViewTitle
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_GeneralScreen() {
    GeneralScreen(
        state = GeneralState(),
    )
}

@Composable
fun GeneralScreen() {
    val viewModel: GeneralViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    val focus = remember { FocusRequester() }
    BackHandler { main.navigation(Nav.POP, null) }
    LaunchedEffect(Unit) {
        focus.requestFocus()
        viewModel.sideEffect.collectLatest {
            if (it is SideEffect.Toast) main.showToast(it.message)
        }
    }
    GeneralScreen(
        state = state,
        action = viewModel,
        navDest = { main.navigation(Nav.PUSH, it) },
        focus = focus,
    )
}

@Composable
private fun GeneralScreen(
    state: GeneralState,
    action: GeneralAction? = null,
    navDest: ((Dest) -> Unit)? = null,
    focus: FocusRequester? = null,
) {
    Column(modifier = Modifier.background(ColorSet.background)) {
        ViewTitle(
            title = stringResource(id = Dest.GENERAL.title),
            navDest = { navDest?.invoke(it) },
        )
        Box(modifier = Modifier.weight(1f)) {
            CalculateView(
                state = state,
                focus = focus,
            )
        }
        KeyView(
            state = state,
            action = action,
        )
    }
}

@Composable
private fun CalculateView(
    state: GeneralState,
    focus: FocusRequester? = null,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            ViewFieldGeneral(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .focusRequester(focus ?: FocusRequester()),
                value = state.value,
            )
        }
        ViewTextResult(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 26.dp),
            text =
                if (state.value.text == "") {
                    ""
                } else {
                    state.result
                },
            fontSizeRange =
                FontSizeRange(
                    min = 1.sp,
                    max = 34.sp,
                ),
        )
    }
}

@Composable
private fun KeyView(
    state: GeneralState,
    action: GeneralAction? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val keyList =
        listOf(
            listOf(
                GeneralKey.Clear,
                GeneralKey.Paste(),
                GeneralKey.Seven,
                GeneralKey.Four,
                GeneralKey.One,
                GeneralKey.Copy,
            ),
            listOf(
                GeneralKey.Left,
                GeneralKey.Open,
                GeneralKey.Eight,
                GeneralKey.Five,
                GeneralKey.Two,
                GeneralKey.Zero,
            ),
            listOf(
                GeneralKey.Right,
                GeneralKey.Close,
                GeneralKey.Nine,
                GeneralKey.Six,
                GeneralKey.Three,
                GeneralKey.Decimal,
            ),
            listOf(
                GeneralKey.Backspace,
                GeneralKey.History,
                GeneralKey.Divide,
                GeneralKey.Multiply,
                GeneralKey.Minus,
                GeneralKey.Plus,
            ),
        )
    val height = keyList.first().count() * InputKeyHeight.value
    Row(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .height(height.dp),
    ) {
        keyList.forEach { row ->
            Column(modifier = Modifier.weight(1f)) {
                row.forEach { key ->
                    ViewButtonKey(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(2.dp),
                        text = key.value,
                        icon =
                            when (key) {
                                is GeneralKey.Left -> key.value.toInt() to 32.dp
                                is GeneralKey.Right -> key.value.toInt() to 32.dp
                                is GeneralKey.Copy -> key.value.toInt() to 26.dp
                                is GeneralKey.Paste -> key.value.toInt() to 24.dp
                                is GeneralKey.History -> key.value.toInt() to 28.dp
                                is GeneralKey.Backspace -> key.value.toInt() to 32.dp
                                else -> null
                            },
                        onClick = {
                            when (key) {
                                is GeneralKey.Copy -> {
                                    val copyStr = state.result.replace(",", "")
                                    clipboardManager.setText(AnnotatedString(copyStr))
                                }
                                is GeneralKey.Paste ->
                                    action?.inputKey(
                                        GeneralKey.Paste(clipboardManager.getText().toString()),
                                    )
                                is GeneralKey.History -> {}
                                else -> action?.inputKey(key)
                            }
                        },
                    )
                }
            }
        }
    }
}
