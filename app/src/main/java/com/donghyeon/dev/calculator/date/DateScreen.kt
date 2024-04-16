package com.donghyeon.dev.calculator.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.MainAction
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.DateType
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.FontSizeRange
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewKeyboard
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
                        value = TextFieldValue(),
                        color = ColorSet.select,
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
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            text = stringResource(id = R.string.day),
                            style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
                        )
                    }
                    Row {
                        Box(
                            modifier =
                                Modifier
                                    .padding(vertical = 15.dp)
                                    .size(50.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(id = R.string.before),
                                style = TextSet.extraBold.copy(ColorSet.select, 30.sp),
                            )
                        }
                        Spacer(modifier = Modifier.width(30.dp))
                        Box(
                            modifier =
                                Modifier
                                    .padding(vertical = 15.dp)
                                    .size(50.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(id = R.string.after),
                                style = TextSet.extraBold.copy(ColorSet.text.copy(alpha = 0.5f), 30.sp),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
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
        } ?: Spacer(modifier = Modifier.weight(1f))
        ViewKeyboard {}
        Spacer(modifier = Modifier.height(3.dp))
    }
}
