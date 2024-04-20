package com.donghyeon.dev.calculator.date

import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.MainAction
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.DateType
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.FontSizeRange
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.donghyeon.dev.calculator.view.ViewTextResult

@Preview
@Composable
private fun Preview_DateScreen_Null() =
    DateScreen(
        state =
            DateState(
                type = null,
            ),
    )

@Preview
@Composable
private fun Preview_DateScreen_DateSearch() =
    DateScreen(
        state =
            DateState(
                type = DateType.DATE_SEARCH,
            ),
    )

@Preview
@Composable
private fun Preview_DateScreen_DateConvert() =
    DateScreen(
        state =
            DateState(
                type = DateType.DATE_CONVERT,
            ),
    )

@Preview
@Composable
private fun Preview_DateScreen_TimeConvert() =
    DateScreen(
        state =
            DateState(
                type = DateType.TIME_COMVERT,
            ),
    )

@Composable
fun DateScreen(
    state: DateState,
    action: DateAction? = null,
    mainAction: MainAction? = null,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    val focus = remember { FocusRequester() }
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
            when (type) {
                DateType.DATE_SEARCH -> {
                    ViewFieldNumber(
                        modifier = Modifier.width(230.dp),
                        value = state.date,
                        color = ColorSet.select,
                        align = TextAlign.Center,
                    )
                    Row(
                        modifier = Modifier.width(230.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        ViewFieldNumber(
                            modifier = Modifier.weight(1f),
                            value = TextFieldValue(),
                            color = ColorSet.text,
                        )
                        Text(
                            modifier = Modifier.width(50.dp).padding(bottom = 15.dp),
                            text = stringResource(id = R.string.day),
                            style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Row(
                        modifier = Modifier.height(100.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.ago),
                            style =
                                TextSet.extraBold.copy(
                                    ColorSet.text.copy(alpha = 0.5f),
                                    22.sp,
                                ),
                        )
                        Spacer(modifier = Modifier.width(30.dp))
                        Text(
                            text = stringResource(id = R.string.later),
                            style =
                                TextSet.extraBold.copy(
                                    ColorSet.select,
                                    30.sp,
                                ),
                        )
                    }
                    ViewTextResult(
                        modifier = Modifier.width(300.dp),
                        text = "?",
                        fontSizeRange =
                            FontSizeRange(
                                min = 1.sp,
                                max = 30.sp,
                            ),
                    )
                }
                DateType.DATE_CONVERT -> {
                    ViewFieldNumber(
                        modifier = Modifier.width(230.dp),
                        value = TextFieldValue(),
                        color = ColorSet.select,
                    )
                    ViewFieldNumber(
                        modifier = Modifier.width(230.dp),
                        value = TextFieldValue(),
                        color = ColorSet.text,
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    ViewTextResult(
                        modifier = Modifier.width(300.dp),
                        text = "?",
                        fontSizeRange =
                            FontSizeRange(
                                min = 1.sp,
                                max = 30.sp,
                            ),
                    )
                }
                DateType.TIME_COMVERT -> {
                    listOf(
                        stringResource(id = R.string.second),
                        stringResource(id = R.string.min),
                        stringResource(id = R.string.hour),
                        stringResource(id = R.string.day),
                    ).forEachIndexed { i, it ->
                        val color =
                            if (i == 0) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            }
                        Row(verticalAlignment = Alignment.Bottom) {
                            ViewFieldNumber(
                                modifier = Modifier.padding(start = 50.dp).width(200.dp),
                                value = TextFieldValue(),
                                color = color,
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                modifier = Modifier.padding(bottom = 10.dp).width(50.dp),
                                text = it,
                                style = TextSet.extraBold.copy(color, 20.sp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier =
                            Modifier
                                .padding(start = 50.dp)
                                .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ViewTextResult(
                            text = "?",
                            fontSizeRange =
                                FontSizeRange(
                                    min = 1.sp,
                                    max = 30.sp,
                                ),
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            modifier = Modifier.width(50.dp),
                            text = "",
                            style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
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
                tabs = stringArrayResource(id = R.array.date_type).toList(),
                index = type.ordinal,
                onTab = { action?.inputType(it) },
            )
            KeyView {}
        } ?: Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(3.dp))
    }
}

@Composable
private fun KeyView(input: (DateKey) -> Unit = {}) {
    val keyList1 =
        listOf(
            DateKey.Clear,
            DateKey.Left,
            DateKey.Right,
            DateKey.Backspace,
        )
    val keyList2 =
        listOf(
            listOf(
                DateKey.Seven,
                DateKey.Eight,
                DateKey.Nine,
            ),
            listOf(
                DateKey.Four,
                DateKey.Five,
                DateKey.Six,
            ),
            listOf(
                DateKey.One,
                DateKey.Two,
                DateKey.Three,
            ),
            listOf(
                DateKey.ZeroZero,
                DateKey.Zero,
                DateKey.Unit,
            ),
        )
    val keyList3 =
        listOf(
            DateKey.Copy,
            DateKey.Paste(""),
            DateKey.Enter,
        )
    val viewButtonKey: @Composable RowScope.(DateKey) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .height(InputKeyHeight.value.dp),
            text = it.value,
            icon =
                when (it) {
                    is DateKey.Left -> it.value.toInt() to 32.dp
                    is DateKey.Right -> it.value.toInt() to 32.dp
                    is DateKey.Backspace -> it.value.toInt() to 32.dp
                    is DateKey.Unit -> it.value.toInt() to 36.dp
                    else -> null
                },
            onClick = { input(it) },
        )
    }
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
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
                val weightList = listOf(1f, 1f, 2f)
                keyList3.forEachIndexed { i, it ->
                    ViewButtonKey(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .weight(weightList[i]),
                        text = it.value,
                        icon =
                            when (it) {
                                is DateKey.Paste -> it.value.toInt() to 28.dp
                                is DateKey.Copy -> it.value.toInt() to 30.dp
                                is DateKey.Enter -> it.value.toInt() to 36.dp
                                else -> null
                            },
                        onClick = { input(it) },
                    )
                }
            }
        }
    }
}
