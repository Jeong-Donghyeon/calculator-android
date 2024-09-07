package com.donghyeon.dev.calculator.date

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.donghyeon.dev.calculator.view.ViewFieldDate
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.donghyeon.dev.calculator.view.ViewTextResult

@Preview
@Composable
private fun Preview_DateScreen_DateSearch() {
    DateScreen(
        state =
            DateState(
                type = DateType.DATE_DAY_DATE,
            ),
        dateDayDateState = DateDayDateState(),
        dateDateDayState = DateDateDayState(),
    )
}

@Preview
@Composable
private fun Preview_DateScreen_DateConvert() {
    DateScreen(
        state =
            DateState(
                type = DateType.DATE_DATE_DAY,
            ),
        dateDayDateState = DateDayDateState(),
        dateDateDayState = DateDateDayState(),
    )
}

@Preview
@Composable
private fun Preview_DateScreen_TimeConvert() {
    DateScreen(
        state =
            DateState(
                type = DateType.TIME_COMVERT,
            ),
        dateDayDateState = DateDayDateState(),
        dateDateDayState = DateDateDayState(),
    )
}

@Composable
fun DateScreen(
    state: DateState,
    dateDayDateState: DateDayDateState,
    dateDateDayState: DateDateDayState,
    action: DateAction? = null,
    mainAction: MainAction? = null,
) {
    val focusManager = LocalFocusManager.current
    val focusDateDayDateDate = remember { FocusRequester() }
    val focusDateDayDateDay = remember { FocusRequester() }
    val focusDateDayDate = {
        state.type?.let {
            if (it == DateType.DATE_DAY_DATE) {
                when (dateDayDateState.focus) {
                    DateDayDateState.Focus.DATE -> focusDateDayDateDate.requestFocus()
                    DateDayDateState.Focus.DAY -> focusDateDayDateDay.requestFocus()
                    DateDayDateState.Focus.AGO_LATER -> focusManager.clearFocus()
                }
            }
        }
    }
    LaunchedEffect(dateDayDateState.focus) {
        focusDateDayDate()
    }
    LaunchedEffect(state.type) {
        focusDateDayDate()
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
            when (type) {
                DateType.DATE_DAY_DATE -> {
                    ViewFieldDate(
                        focus = focusDateDayDateDate,
                        value = dateDayDateState.date,
                        hint = state.hint,
                        color =
                            if (dateDayDateState.focus == DateDayDateState.Focus.DATE) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                    )
                    Row(
                        modifier = Modifier.width(210.dp),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        ViewFieldNumber(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .focusRequester(focusDateDayDateDay),
                            value = dateDayDateState.day,
                            color =
                                if (dateDayDateState.focus == DateDayDateState.Focus.DAY) {
                                    ColorSet.select
                                } else {
                                    ColorSet.text
                                },
                        )
                        Text(
                            modifier =
                                Modifier
                                    .width(50.dp)
                                    .padding(bottom = 15.dp),
                            text = stringResource(id = R.string.day),
                            style = TextSet.bold.copy(ColorSet.text, 20.sp),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Column {
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val (ago, later) =
                                if (dateDayDateState.agoLater) {
                                    TextSet.extraBold.copy(
                                        ColorSet.hint,
                                        24.sp,
                                    ) to
                                        TextSet.extraBold.copy(
                                            ColorSet.select,
                                            26.sp,
                                        )
                                } else {
                                    TextSet.extraBold.copy(
                                        ColorSet.select,
                                        26.sp,
                                    ) to
                                        TextSet.extraBold.copy(
                                            ColorSet.hint,
                                            24.sp,
                                        )
                                }
                            Box(
                                modifier = Modifier.width(105.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = stringResource(id = R.string.ago),
                                    style = ago,
                                )
                            }
                            Box(
                                modifier = Modifier.width(105.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = stringResource(id = R.string.later),
                                    style = later,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier =
                                Modifier
                                    .background(
                                        if (dateDayDateState.focus == DateDayDateState.Focus.AGO_LATER) {
                                            ColorSet.select
                                        } else {
                                            ColorSet.text
                                        },
                                    )
                                    .width(210.dp)
                                    .height(1.dp),
                        )
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                    ViewTextResult(
                        modifier = Modifier.width(300.dp),
                        text = dateDayDateState.result,
                        fontSizeRange =
                            FontSizeRange(
                                min = 1.sp,
                                max = 30.sp,
                            ),
                    )
                }
                DateType.DATE_DATE_DAY -> {
                    ViewFieldNumber(
                        modifier =
                            Modifier
                                .width(230.dp),
                        value = dateDateDayState.date1,
                        color = ColorSet.select,
                    )
                    ViewFieldNumber(
                        modifier = Modifier.width(230.dp),
                        value = dateDateDayState.date2,
                        color = ColorSet.text,
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    ViewTextResult(
                        modifier = Modifier.width(300.dp),
                        text = dateDateDayState.result,
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
                        val defaultModifier =
                            Modifier
                                .padding(start = 50.dp)
                                .width(200.dp)
                        val (modifier, color) =
                            if (i == 0) {
                                defaultModifier to ColorSet.select
                            } else {
                                defaultModifier to ColorSet.text
                            }
                        Row(verticalAlignment = Alignment.Bottom) {
                            ViewFieldNumber(
                                modifier = modifier,
                                value = TextFieldValue(),
                                color = color,
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                modifier =
                                    Modifier
                                        .padding(bottom = 10.dp)
                                        .width(50.dp),
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
        } ?: Spacer(modifier = Modifier.weight(1f))
        KeyView { action?.inputKey(it) }
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
                DateKey.Today,
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
                    is DateKey.Today -> it.value.toInt() to 30.dp
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
