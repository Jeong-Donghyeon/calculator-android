package com.donghyeon.dev.calculator.general

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
private fun Preview_GeneralScreen() =
    GeneralScreen(
        state = GeneralState(),
    )

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
                    .focusRequester(focus ?: FocusRequester()),
            value = state.value,
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier =
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
            contentAlignment = Alignment.Center,
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
        Spacer(modifier = Modifier.height(20.dp))
        GeneralKeyView(
            state = state,
            action = action,
        )
    }
}

@Composable
private fun GeneralKeyView(
    state: GeneralState,
    action: GeneralAction? = null,
) {
    val keyList1 =
        listOf(
            listOf(
                GeneralKey.Clear,
                GeneralKey.Left,
                GeneralKey.Right,
                GeneralKey.Backspace,
            ),
            listOf(
                GeneralKey.History,
                GeneralKey.Open,
                GeneralKey.Close,
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
                    is GeneralKey.History -> it.value.toInt() to 28.dp
                    is GeneralKey.Backspace -> it.value.toInt() to 32.dp
                    else -> null
                },
            onClick = {
                when (it) {
                    is GeneralKey.History -> action?.history()
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
            Row(modifier = Modifier.fillMaxWidth()) {
                keyList.forEach { viewButtonKey(it) }
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            val scrollState = rememberScrollState()
            LaunchedEffect(key1 = state.history) {
                scrollState.scrollTo(scrollState.maxValue)
            }
            Column(modifier = Modifier.weight(3f)) {
                if (state.history) {
                    val height = keyList2.count() * InputKeyHeight.value - 4
                    Box(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(ColorSet.button)
                                .fillMaxWidth()
                                .height(height.dp),
                    ) {
                        Column(modifier = Modifier.verticalScroll(scrollState)) {
                            Spacer(modifier = Modifier.height(10.dp))
                            state.historyList.forEach {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                                    Text(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .clickable {},
                                        text = it.first,
                                        textAlign = TextAlign.End,
                                        style = TextSet.bold.copy(ColorSet.text, 18.sp),
                                    )
                                    Text(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .clickable {},
                                        text = it.second,
                                        textAlign = TextAlign.End,
                                        style = TextSet.bold.copy(ColorSet.result, 19.sp),
                                    )
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
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                        IconButton(onClick = { action?.clearHistory() }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.ic_cancel_24px),
                                tint = ColorSet.text,
                                contentDescription = "Delete",
                            )
                        }
                    }
                } else {
                    keyList2.forEach { keyList ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            keyList.forEach { viewButtonKey(it) }
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
