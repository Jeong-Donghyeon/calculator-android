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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet
import dev.donghyeon.calculator.view.TitleView
import dev.donghyeon.calculator.view.ViewButtonNumber
import dev.donghyeon.calculator.view.ViewButtonValue
import dev.donghyeon.calculator.view.ViewScrollTab
import dev.donghyeon.calculator.view.ViewTextField

@Preview
@Composable
fun Preview_PercentScreen() =
    PercentScreen(
        state = PercentData(),
    )

@Composable
fun PercentScreen() {
    val viewModel: PercentViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
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
        when (state.select) {
            PercentSelect.CALCULATE1 -> Calculate11View(state = state)
            PercentSelect.CALCULATE2 -> Calculate2View(state = state)
            PercentSelect.CALCULATE3 -> Calculate3View(state = state)
            PercentSelect.CALCULATE4 -> Calculate4View(state = state)
        }
    }
    Row(verticalAlignment = Alignment.Bottom) {
        ViewButtonNumber(
            modifier = Modifier.padding(start = 10.dp),
            text = "메뉴",
            height = 40.dp,
            size = 20.sp,
            onClick = {},
        )
        ViewScrollTab(
            modifier = Modifier.fillMaxWidth(),
            tabs = PercentSelect.entries.map { it.value },
            index = state.select.ordinal,
            onTab = {
                when (it) {
                    0 -> action?.inputPercentSelect(PercentSelect.CALCULATE1)
                    1 -> action?.inputPercentSelect(PercentSelect.CALCULATE2)
                    2 -> action?.inputPercentSelect(PercentSelect.CALCULATE3)
                    3 -> action?.inputPercentSelect(PercentSelect.CALCULATE4)
                }
            },
        )
    }
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
fun Calculate11View(state: PercentData) {
    val fieldTotalWith: Dp = 320.dp
    val fieldLeft: Dp = 50.dp
    val fieldRight: Dp = 60.dp
    val v1Focus = remember { FocusRequester() }
    val v2Focus = remember { FocusRequester() }
    val guideStrList =
        when (state.select) {
            PercentSelect.CALCULATE1 ->
                listOf(
                    "의",
                    "% 는",
                    "예) 100 의 10% 는 10",
                )
            PercentSelect.CALCULATE2 ->
                listOf(
                    "의",
                    "은",
                    "예) 100 의 10 은 10%",
                )
            PercentSelect.CALCULATE3 ->
                listOf(
                    "이/가",
                    "(으)로\n변하면",
                    "예) 100 이 10 으로 변하면 90% 감소",
                )
            PercentSelect.CALCULATE4 ->
                listOf(
                    "이/가",
                    "%\n증가하면",
                    "예) 100 이 10% 증가하면 1000",
                )
        }
    val calculate =
        when (state.select) {
            PercentSelect.CALCULATE1 -> state.calculate1
            PercentSelect.CALCULATE2 -> state.calculate2
            PercentSelect.CALCULATE3 -> state.calculate3
            PercentSelect.CALCULATE4 -> state.calculate4
        }
    val (v1Color, v2Color) =
        if (calculate.select == ValueSelect.V1) {
            ColorSet.select to ColorSet.text
        } else {
            ColorSet.text to ColorSet.select
        }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.width(fieldTotalWith),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.width(fieldLeft),
                text = "V1",
                style = TextSet.extraBold.copy(v1Color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewTextField(
                modifier = Modifier.weight(1f).focusRequester(v1Focus),
                value = calculate.v1,
                color = v1Color,
                onValueChange = {},
            )
            Text(
                modifier = Modifier.width(fieldRight).padding(start = 10.dp),
                text = guideStrList[0],
                style = TextSet.extraBold.copy(v1Color, 20.sp),
                textAlign = TextAlign.Start,
            )
        }
        Row(
            modifier = Modifier.width(fieldTotalWith),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.width(fieldLeft),
                text = "V2",
                style = TextSet.extraBold.copy(v2Color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewTextField(
                modifier = Modifier.weight(1f).focusRequester(v2Focus),
                value = calculate.v2,
                color = v2Color,
                onValueChange = {},
            )
            Text(
                modifier = Modifier.width(fieldRight).padding(start = 10.dp),
                text = guideStrList[1],
                style = TextSet.extraBold.copy(v2Color, 20.sp),
                textAlign = TextAlign.Start,
            )
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
                    text = calculate.result,
                    style = TextSet.extraBold.copy(ColorSet.result, 34.sp),
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = guideStrList[2],
                style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
            )
        }
    }
    LaunchedEffect(calculate.select) {
        if (calculate.select == ValueSelect.V2) {
            v2Focus.requestFocus()
        } else {
            v1Focus.requestFocus()
        }
    }
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V1",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate1.select == ValueSelect.V1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
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
                                if (state.calculate1.select == ValueSelect.V1) {
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
                                    if (state.calculate1.select == ValueSelect.V1) {
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
                            if (state.calculate1.select == ValueSelect.V1) {
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V2",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate1.select == ValueSelect.V2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }

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
                                if (state.calculate1.select == ValueSelect.V2) {
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
                                    if (state.calculate1.select == ValueSelect.V2) {
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
                            if (state.calculate1.select == ValueSelect.V2) {
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
                text = "예) 100 의 10% 는 10",
                style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
            )
        }
    }

@Composable
private fun Calculate2View(state: PercentData) =
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V1",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate2.select == ValueSelect.V1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
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
                    text = state.calculate2.value1,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.calculate2.select == ValueSelect.V1) {
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
                                    if (state.calculate2.select == ValueSelect.V1) {
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
                            if (state.calculate2.select == ValueSelect.V1) {
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V2",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate2.select == ValueSelect.V2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
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
                    text = state.calculate2.value2,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.calculate2.select == ValueSelect.V2) {
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
                                    if (state.calculate2.select == ValueSelect.V2) {
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
                    text = "은",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate2.select == ValueSelect.V2) {
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
                    text = state.calculate2.result,
                    style = TextSet.extraBold.copy(ColorSet.result, 34.sp),
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = "예) 100 의 10 은 10%",
                style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
            )
        }
    }

@Composable
private fun Calculate3View(state: PercentData) =
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V1",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate3.select == ValueSelect.V1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
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
                    text = state.calculate3.value1,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.calculate3.select == ValueSelect.V1) {
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
                                    if (state.calculate3.select == ValueSelect.V1) {
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
                    text = "이/가",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate3.select == ValueSelect.V1) {
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V2",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate3.select == ValueSelect.V2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
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
                    text = state.calculate3.value2,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.calculate3.select == ValueSelect.V2) {
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
                                    if (state.calculate3.select == ValueSelect.V2) {
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
                    text = "(으)로\n변하면",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate3.select == ValueSelect.V2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            14.sp,
                            lineHeight = 16.sp,
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
                    text = state.calculate3.result,
                    style = TextSet.extraBold.copy(ColorSet.result, 34.sp),
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = "예) 100 이 10 으로 변하면 90% 감소",
                style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
            )
        }
    }

@Composable
private fun Calculate4View(state: PercentData) =
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V1",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate4.select == ValueSelect.V1) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
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
                    text = state.calculate4.value1,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.calculate4.select == ValueSelect.V1) {
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
                                    if (state.calculate4.select == ValueSelect.V1) {
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
                    text = "이/가",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate4.select == ValueSelect.V1) {
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
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.weight(1f))
                Text(
                    text = "V2",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate4.select == ValueSelect.V2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            24.sp,
                        ),
                    textAlign = TextAlign.Center,
                )
            }
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
                    text = state.calculate4.value2,
                    style =
                        TextSet.extraBold.copy(
                            color =
                                if (state.calculate4.select == ValueSelect.V2) {
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
                                    if (state.calculate4.select == ValueSelect.V2) {
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
                    text = "%\n증가하면",
                    style =
                        TextSet.extraBold.copy(
                            if (state.calculate4.select == ValueSelect.V2) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                            14.sp,
                            lineHeight = 16.sp,
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
                    text = state.calculate4.result,
                    style = TextSet.extraBold.copy(ColorSet.result, 34.sp),
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = "예) 100 이 10% 증가하면 100",
                style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
            )
        }
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
                onClick = { action?.inputNumberKeyPad(NumberPadKey.CLEAR) },
                text = "C",
            )
            ViewButtonValue(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(2.dp),
                onClick = { action?.inputNumberKeyPad(NumberPadKey.LEFT) },
                text = "<-",
                style = TextSet.bold.copy(ColorSet.text, 26.sp),
            )
            ViewButtonValue(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(2.dp),
                onClick = { action?.inputNumberKeyPad(NumberPadKey.RIGHT) },
                text = "->",
                style = TextSet.bold.copy(ColorSet.text, 26.sp),
            )
        }
        listOf(
            listOf(NumberPadKey.SEVEN, NumberPadKey.EIGHT, NumberPadKey.NINE),
            listOf(NumberPadKey.FOUR, NumberPadKey.FIVE, NumberPadKey.SIX),
            listOf(NumberPadKey.ONE, NumberPadKey.TWO, NumberPadKey.THREE),
            listOf(NumberPadKey.ZERO3, NumberPadKey.ZERO, NumberPadKey.DECIMAL),
        ).forEach {
            Row(modifier = Modifier.weight(1f)) {
                it.forEach {
                    ViewButtonNumber(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                        onClick = { action?.inputNumberKeyPad(it) },
                        text = it.value,
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
        onClick = { action?.inputNumberKeyPad(NumberPadKey.BACK) },
        text = "⌫",
        size = 26.sp,
    )
    ViewButtonValue(
        modifier =
            Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(2.dp),
        onClick = { action?.inputValueSelect(ValueSelect.V1) },
        text = "V1",
        style =
            TextSet.extraBold.copy(
                color =
                    when (state.select) {
                        PercentSelect.CALCULATE1 -> if (state.calculate1.select == ValueSelect.V1) ColorSet.select else ColorSet.text
                        PercentSelect.CALCULATE2 -> if (state.calculate2.select == ValueSelect.V1) ColorSet.select else ColorSet.text
                        PercentSelect.CALCULATE3 -> if (state.calculate3.select == ValueSelect.V1) ColorSet.select else ColorSet.text
                        PercentSelect.CALCULATE4 -> if (state.calculate4.select == ValueSelect.V1) ColorSet.select else ColorSet.text
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
        onClick = { action?.inputValueSelect(ValueSelect.V2) },
        text = "V2",
        style =
            TextSet.extraBold.copy(
                color =
                    when (state.select) {
                        PercentSelect.CALCULATE1 -> if (state.calculate1.select == ValueSelect.V2) ColorSet.select else ColorSet.text
                        PercentSelect.CALCULATE2 -> if (state.calculate2.select == ValueSelect.V2) ColorSet.select else ColorSet.text
                        PercentSelect.CALCULATE3 -> if (state.calculate3.select == ValueSelect.V2) ColorSet.select else ColorSet.text
                        PercentSelect.CALCULATE4 -> if (state.calculate4.select == ValueSelect.V2) ColorSet.select else ColorSet.text
                    },
                24.sp,
            ),
    )
}
