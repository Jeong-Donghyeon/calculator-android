package com.donghyeon.dev.calculator.general

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.MainAction
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.FontSizeRange
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewFieldGeneral
import com.donghyeon.dev.calculator.view.ViewTextResult
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_GeneralScreen() {
    GeneralScreen(
        state =
            GeneralState(
                value = TextFieldValue("1+1", TextRange(3)),
                result = "2",
            ),
    )
}

@Preview
@Composable
private fun Preview_GeneralScreen_History() {
    GeneralScreen(
        state =
            GeneralState(
                history = true,
                historyList =
                    listOf(
                        "1+1" to "2",
                        "2+2" to "4",
                        "3+3" to "6",
                    ),
                value = TextFieldValue("4+4", TextRange(3)),
                result = "8",
            ),
    )
}

@Composable
fun GeneralScreen(
    state: GeneralState,
    action: GeneralAction? = null,
    mainAction: MainAction? = null,
) {
    val context = LocalContext.current
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focus.requestFocus()
        action?.sideEffect?.collectLatest {
            if (it is SideEffect.Toast) {
                mainAction?.showToast(context.getString(it.id))
            }
        }
    }
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        ViewFieldGeneral(
            modifier =
                Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth()
                    .focusRequester(focus),
            value = state.value,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier =
                Modifier
                    .padding(horizontal = 23.dp)
                    .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            ViewTextResult(
                text =
                    if (state.value.text == "") {
                        ""
                    } else {
                        state.result
                    },
                fontSizeRange =
                    FontSizeRange(
                        min = 1.sp,
                        max = 30.sp,
                    ),
            )
        }
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(bottom = 7.dp),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = { action?.history() }) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_history_24px),
                        tint = ColorSet.text,
                        contentDescription = "History",
                    )
                }
            }
            Spacer(modifier = Modifier.weight(3f))
        }
        KeyView(
            state = state,
            action = action,
        )
    }
}

@Composable
private fun KeyView(
    state: GeneralState,
    action: GeneralAction? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val keyList1 =
        listOf(
            listOf(
                GeneralKey.Clear,
                GeneralKey.Left,
                GeneralKey.Right,
                GeneralKey.Backspace,
            ),
            listOf(
                GeneralKey.Paste(""),
                GeneralKey.Copy,
                GeneralKey.Bracket,
                GeneralKey.Divide,
            ),
        )
    val keyList2 =
        listOf(
            listOf(
                GeneralKey.Seven,
                GeneralKey.Eight,
                GeneralKey.Nine,
            ),
            listOf(
                GeneralKey.Four,
                GeneralKey.Five,
                GeneralKey.Six,
            ),
            listOf(
                GeneralKey.One,
                GeneralKey.Two,
                GeneralKey.Three,
            ),
            listOf(
                GeneralKey.ZeroZero,
                GeneralKey.Zero,
                GeneralKey.Decimal,
            ),
        )
    val keyList3 =
        listOf(
            GeneralKey.Multiply,
            GeneralKey.Minus,
            GeneralKey.Plus,
            GeneralKey.Equal,
        )
    val viewButtonKey: @Composable RowScope.(GeneralKey) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .height(InputKeyHeight.value.dp),
            text = it.value,
            icon =
                when (it) {
                    is GeneralKey.Left -> it.value.toInt() to 32.dp
                    is GeneralKey.Right -> it.value.toInt() to 32.dp
                    is GeneralKey.Backspace -> it.value.toInt() to 32.dp
                    is GeneralKey.Copy -> it.value.toInt() to 30.dp
                    is GeneralKey.Paste -> it.value.toInt() to 28.dp
                    else -> null
                },
            onClick = {
                when (it) {
                    is GeneralKey.History -> action?.history()
                    is GeneralKey.Copy -> {
                        val copyStr = state.result.replace(",", "")
                        clipboardManager.setText(AnnotatedString(copyStr))
                    }
                    is GeneralKey.Paste ->
                        action?.inputKey(
                            GeneralKey.Paste(clipboardManager.getText().toString()),
                        )
                    else -> action?.inputKey(it)
                }
            },
        )
    }
    Column(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 3.dp),
    ) {
        keyList1.forEach { keyList ->
            Row {
                keyList.forEach { viewButtonKey(it) }
            }
        }
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            val scrollState = rememberScrollState()
            LaunchedEffect(key1 = state.history) {
                scrollState.scrollTo(scrollState.maxValue)
            }
            Box(modifier = Modifier.weight(3f)) {
                Column {
                    keyList2.forEach { keyList ->
                        Row {
                            keyList.forEach { viewButtonKey(it) }
                        }
                    }
                }
                if (state.history) {
                    val height = InputKeyHeight * keyList2.count() + 12.dp
                    Box(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(ColorSet.button)
                                .fillMaxWidth()
                                .height(height),
                    ) {
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            Spacer(modifier = Modifier.height(5.dp))
                            state.historyList.forEach {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                                    Row {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            modifier = Modifier.clickable {},
                                            text = it.first,
                                            textAlign = TextAlign.End,
                                            style = TextSet.bold.copy(ColorSet.text, 18.sp),
                                        )
                                    }
                                    Row {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            modifier = Modifier.clickable {},
                                            text = it.second,
                                            textAlign = TextAlign.End,
                                            style = TextSet.bold.copy(ColorSet.result, 19.sp),
                                        )
                                    }
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .padding(horizontal = 5.dp)
                                            .background(ColorSet.text.copy(alpha = 0.2f))
                                            .fillMaxWidth()
                                            .height(1.dp),
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        IconButton(onClick = { action?.clearHistory() }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.ic_cancel_24px),
                                tint = ColorSet.text,
                                contentDescription = "Clear",
                            )
                        }
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                keyList3.forEach {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        viewButtonKey(it)
                    }
                }
            }
        }
    }
}
