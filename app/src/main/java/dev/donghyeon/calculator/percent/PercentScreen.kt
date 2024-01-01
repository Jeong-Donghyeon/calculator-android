package dev.donghyeon.calculator.percent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.common.TitleView
import dev.donghyeon.calculator.common.ViewButtonNumber
import dev.donghyeon.calculator.common.ViewButtonValue
import dev.donghyeon.calculator.common.ViewScrollTab
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_PercentScreen() =
    PercentScreen(
        state = PercentData(),
    )

@Composable
fun PercentScreen() {
    val viewModel: PercentViewModel = hiltViewModel()
    val state by viewModel.percentState.collectAsState()
    PercentScreen(
        state = state,
        action = viewModel,
    )
}

const val RATIO_KEYBOARD = 3f

@Composable
fun PercentScreen(
    state: PercentData,
    action: PercentAction? = null,
) = Column(modifier = Modifier.background(ColorSet.container)) {
    TitleView(title = "퍼센트 계산기")
    Column(modifier = Modifier.weight(1f)) {
        when (state.tab + 1) {
            1 -> Calculate1View(state = state)
            2 -> {}
            3 -> {}
            4 -> {}
            else -> {}
        }
    }
    ViewScrollTab(
        modifier = Modifier.fillMaxWidth(),
        tabs = listOf("비율값", "일부값", "증감값", "증감율"),
        index = state.tab,
        onTab = { action?.inputTab(it) },
    )
    Row(
        modifier =
            Modifier
                .padding(10.dp)
                .padding(bottom = 10.dp),
    ) {
        Column(modifier = Modifier.weight(RATIO_KEYBOARD)) {
            KeyboardLeftView(action = action)
        }
        Column(modifier = Modifier.weight(1f)) {
            KeyboardRightView(state = state, action = action)
        }
    }
}

@Composable
private fun Calculate2View(state: PercentData) =
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row {
            Text(
                text = "전체값  ",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 2) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = state.calculate2.value1,
                style =
                    TextSet.extraBold.copy(
                        color =
                            if (state.tab + 1 == 2 && state.calculate2.valueSelect == 1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = "  의  ",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 2) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = state.calculate2.value2,
                style =
                    TextSet.extraBold.copy(
                        color =
                            if (state.tab + 1 == 2 && state.calculate2.valueSelect == 2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = " 은  몇 % ?",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 2) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = state.calculate2.result,
            style =
                TextSet.extraBold.copy(
                    color = if (state.tab + 1 == 2) ColorSet.result else ColorSet.text,
                    fontSize = 20.sp,
                ),
        )
    }

@Composable
private fun Calculate3View(state: PercentData) =
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row {
            Text(
                text = "전체값  ",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 3) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = state.calculate3.value1,
                style =
                    TextSet.extraBold.copy(
                        color =
                            if (state.tab + 1 == 3 && state.calculate3.valueSelect == 1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = "  이(가)  ",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 3) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = state.calculate3.value2,
                style =
                    TextSet.extraBold.copy(
                        color =
                            if (state.tab + 1 == 3 && state.calculate3.valueSelect == 2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = " 으로 변하면 ?",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 3) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = state.calculate3.result,
            style =
                TextSet.extraBold.copy(
                    color = if (state.tab + 1 == 3) ColorSet.result else ColorSet.text,
                    fontSize = 20.sp,
                ),
        )
    }

@Composable
private fun Calculate4View(state: PercentData) =
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row {
            Text(
                text = "전체값  ",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 4) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = state.calculate4.value1,
                style =
                    TextSet.extraBold.copy(
                        color =
                            if (state.tab + 1 == 4 && state.calculate4.valueSelect == 1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = "  이(가)  ",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 4) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = state.calculate4.value2,
                style =
                    TextSet.extraBold.copy(
                        color =
                            if (state.tab + 1 == 4 && state.calculate4.valueSelect == 2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                        fontSize = 17.sp,
                    ),
            )
            Text(
                text = " % 증가하면 ?",
                style =
                    TextSet.extraBold.copy(
                        color = if (state.tab + 1 == 4) ColorSet.blue else ColorSet.text,
                        fontSize = 17.sp,
                    ),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = state.calculate4.result,
            style =
                TextSet.extraBold.copy(
                    color = if (state.tab + 1 == 4) ColorSet.result else ColorSet.text,
                    fontSize = 20.sp,
                ),
        )
    }

@Composable
fun KeyboardLeftView(action: PercentAction? = null) =
    Column(modifier = Modifier.height(350.dp)) {
        Row(modifier = Modifier.weight(1f)) {
            ViewButtonNumber(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(2.dp),
                onClick = { action?.inputClear() },
                text = "c",
            )
            ViewButtonValue(
                modifier =
                    Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .padding(2.dp),
                onClick = {},
                text = "메뉴",
                style = TextSet.bold.copy(ColorSet.text, 20.sp),
            )
        }
        listOf(
            listOf("7", "8", "9"),
            listOf("4", "5", "6"),
            listOf("1", "2", "3"),
            listOf("000", "0", "."),
        ).forEach {
            Row(modifier = Modifier.weight(1f)) {
                it.forEach {
                    ViewButtonNumber(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                        onClick = { action?.inputNumber(it) },
                        text = it,
                    )
                }
            }
        }
    }

@Composable
private fun KeyboardRightView(
    state: PercentData,
    action: PercentAction? = null,
) = Column(modifier = Modifier.height(350.dp)) {
    ViewButtonNumber(
        modifier =
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(2.dp),
        onClick = { action?.inputBack() },
        text = "<",
    )
    ViewButtonValue(
        modifier =
            Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(2.dp),
        onClick = { action?.inputValueSelect(num = 1) },
        text = "값1",
        style =
            TextSet.extraBold.copy(
                color =
                    when (state.tab + 1) {
                        1 -> if (state.calculate1.valueSelect == 1) ColorSet.select else ColorSet.text
                        2 -> if (state.calculate2.valueSelect == 1) ColorSet.select else ColorSet.text
                        3 -> if (state.calculate3.valueSelect == 1) ColorSet.select else ColorSet.text
                        4 -> if (state.calculate4.valueSelect == 1) ColorSet.select else ColorSet.text
                        else -> ColorSet.text
                    },
                24.sp,
            ),
    )
    ViewButtonValue(
        modifier =
            Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(2.dp),
        onClick = { action?.inputValueSelect(num = 2) },
        text = "값2",
        style =
            TextSet.extraBold.copy(
                color =
                    when (state.tab + 1) {
                        1 -> if (state.calculate1.valueSelect == 2) ColorSet.select else ColorSet.text
                        2 -> if (state.calculate2.valueSelect == 2) ColorSet.select else ColorSet.text
                        3 -> if (state.calculate3.valueSelect == 2) ColorSet.select else ColorSet.text
                        4 -> if (state.calculate4.valueSelect == 2) ColorSet.select else ColorSet.text
                        else -> ColorSet.text
                    },
                24.sp,
            ),
    )
}

@Composable
fun Calculate1View(state: PercentData) =
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.width(320.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.width(50.dp),
                text = "V1",
                style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
                textAlign = TextAlign.Center,
            )
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(50.dp),
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                    text = state.calculate1.value1,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.tab + 1 == 1 && state.calculate1.valueSelect == 1) {
                                    ColorSet.select
                                } else {
                                    ColorSet.text
                                },
                            fontSize = 26.sp,
                        ),
                    textAlign = TextAlign.End,
                )
                Box(
                    modifier =
                        Modifier
                            .background(
                                color =
                                    if (state.tab + 1 == 1 && state.calculate1.valueSelect == 1) {
                                        ColorSet.select
                                    } else {
                                        ColorSet.text
                                    },
                            )
                            .fillMaxWidth(1f)
                            .height(2.dp),
                )
            }
            Column(
                modifier =
                    Modifier
                        .width(60.dp)
                        .height(50.dp)
                        .padding(start = 5.dp),
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "의",
                    style =
                        TextSet.extraBold.copy(
                            if (state.tab + 1 == 1 && state.calculate1.valueSelect == 1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            20.sp,
                        ),
                    textAlign = TextAlign.Start,
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.width(320.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.width(50.dp),
                text = "V2",
                style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
                textAlign = TextAlign.Center,
            )
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(50.dp),
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                    text = state.calculate1.value2,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.tab + 1 == 1 && state.calculate1.valueSelect == 2) {
                                    ColorSet.select
                                } else {
                                    ColorSet.text
                                },
                            fontSize = 26.sp,
                        ),
                    textAlign = TextAlign.End,
                )
                Box(
                    modifier =
                        Modifier
                            .background(
                                color =
                                    if (state.tab + 1 == 1 && state.calculate1.valueSelect == 2) {
                                        ColorSet.select
                                    } else {
                                        ColorSet.text
                                    },
                            )
                            .fillMaxWidth(1f)
                            .height(2.dp),
                )
            }
            Column(
                modifier =
                    Modifier
                        .width(60.dp)
                        .height(50.dp)
                        .padding(start = 5.dp),
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "% 는",
                    style =
                        TextSet.extraBold.copy(
                            if (state.tab + 1 == 1 && state.calculate1.valueSelect == 2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            20.sp,
                        ),
                    textAlign = TextAlign.Start,
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.calculate1.result,
                    style = TextSet.extraBold.copy(ColorSet.result, 34.sp),
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = "예) 100의 10% 는 10",
                style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
            )
        }
    }
