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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.R
import dev.donghyeon.calculator.common.BUTTON_HEIGHT
import dev.donghyeon.calculator.common.LocalViewModel
import dev.donghyeon.calculator.common.SideEffect
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet
import dev.donghyeon.calculator.view.FontSizeRange
import dev.donghyeon.calculator.view.TitleView
import dev.donghyeon.calculator.view.ViewButtonNumber
import dev.donghyeon.calculator.view.ViewButtonValue
import dev.donghyeon.calculator.view.ViewScrollTab
import dev.donghyeon.calculator.view.ViewTextField
import dev.donghyeon.calculator.view.ViewTextResult
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun Preview_PercentScreen() {
    PercentScreen(
        state = PercentData(),
    )
}

@Composable
fun PercentScreen() {
    val viewModel: PercentViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    LaunchedEffect(true) {
        viewModel.sideEffect.collectLatest {
            if (it is SideEffect.Toast) main.showToast(it.message)
        }
    }
    PercentScreen(
        state = state,
        action = viewModel,
        menu = { main.openMenu() },
    )
}

@Composable
fun PercentScreen(
    state: PercentData,
    action: PercentAction? = null,
    menu: (() -> Unit)? = null,
) {
    val keyboardHeight = (BUTTON_HEIGHT * 5).dp
    val v1Focus = remember { FocusRequester() }
    val v2Focus = remember { FocusRequester() }
    Column(modifier = Modifier.background(ColorSet.container)) {
        TitleView(title = Destination.Percent.route)
        Box(modifier = Modifier.weight(1f)) {
            CalculateView(
                state = state,
                action = action,
                v1Focus = v1Focus,
                v2Focus = v2Focus,
            )
        }
        Row(verticalAlignment = Alignment.Bottom) {
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .shadow(2.dp)
                        .background(ColorSet.button),
                onClick = menu ?: {},
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_menu),
                    tint = ColorSet.text,
                    contentDescription = "menu",
                )
            }
            ViewScrollTab(
                modifier = Modifier.fillMaxWidth().padding(bottom = 3.dp),
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
            Column(modifier = Modifier.weight(3f)) {
                KeyboardLeftView(
                    action = action,
                    height = keyboardHeight,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                KeyboardRightView(
                    state = state,
                    action = action,
                    height = keyboardHeight,
                    v1Focus = v1Focus,
                    v2Focus = v2Focus,
                )
            }
        }
    }
}

@Composable
fun CalculateView(
    state: PercentData,
    action: PercentAction? = null,
    v1Focus: FocusRequester,
    v2Focus: FocusRequester,
) {
    val fieldTotalWith: Dp = 320.dp
    val fieldLeft: Dp = 50.dp
    val fieldRight: Dp = 60.dp
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
                    "예) 100 이 10% 증가하면 110",
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
        when (calculate.select) {
            ValueSelect.V1 -> ColorSet.select to ColorSet.text
            ValueSelect.V2 -> ColorSet.text to ColorSet.select
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
                modifier =
                    Modifier
                        .width(fieldLeft)
                        .padding(top = 5.dp),
                text = "V1",
                style = TextSet.extraBold.copy(v1Color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewTextField(
                modifier =
                    Modifier
                        .weight(1f)
                        .focusRequester(v1Focus)
                        .onFocusChanged {
                            if (it.isFocused) action?.inputValueSelect(ValueSelect.V1)
                        },
                value = calculate.v1,
                color = v1Color,
                onValueChange = {},
            )
            Text(
                modifier =
                    Modifier
                        .width(fieldRight)
                        .padding(top = 5.dp, start = 10.dp),
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
                modifier =
                    Modifier
                        .width(fieldLeft)
                        .padding(top = 5.dp),
                text = "V2",
                style = TextSet.extraBold.copy(v2Color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewTextField(
                modifier =
                    Modifier
                        .weight(1f)
                        .focusRequester(v2Focus)
                        .onFocusChanged {
                            if (it.isFocused) action?.inputValueSelect(ValueSelect.V2)
                        },
                value = calculate.v2,
                color = v2Color,
                onValueChange = {},
            )
            val v2FontSize =
                when (state.select) {
                    PercentSelect.CALCULATE3 -> 14.sp
                    PercentSelect.CALCULATE4 -> 14.sp
                    else -> 20.sp
                }
            Text(
                modifier =
                    Modifier
                        .width(fieldRight)
                        .padding(top = 5.dp, start = 10.dp),
                text = guideStrList[1],
                style =
                    TextSet.extraBold.copy(
                        v2Color,
                        v2FontSize,
                        lineHeight = 18.sp,
                    ),
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
                ViewTextResult(
                    modifier = Modifier.width(fieldTotalWith),
                    text = calculate.result,
                    fontSizeRange =
                        FontSizeRange(
                            min = 1.sp,
                            max = 34.sp,
                        ),
                )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = guideStrList[2],
                style = TextSet.extraBold.copy(ColorSet.text, 16.sp),
            )
        }
    }
    LaunchedEffect(Unit) {
        v1Focus.requestFocus()
    }
    LaunchedEffect(state.select) {
        when (calculate.select) {
            ValueSelect.V1 -> v1Focus.requestFocus()
            ValueSelect.V2 -> v2Focus.requestFocus()
        }
    }
}

@Composable
fun KeyboardLeftView(
    action: PercentAction? = null,
    height: Dp,
) {
    Column(modifier = Modifier.height(height)) {
        Row(modifier = Modifier.weight(1f)) {
            ViewButtonNumber(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(2.dp),
                onClick = {
                    action?.inputNumberKeyPad(NumberPadKey.CLEAR)
                },
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
            listOf(NumberPadKey.ZERO_ZERO, NumberPadKey.ZERO, NumberPadKey.DECIMAL),
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
}

@Composable
private fun KeyboardRightView(
    state: PercentData,
    action: PercentAction? = null,
    height: Dp,
    v1Focus: FocusRequester,
    v2Focus: FocusRequester,
) {
    val calculate =
        when (state.select) {
            PercentSelect.CALCULATE1 -> state.calculate1
            PercentSelect.CALCULATE2 -> state.calculate2
            PercentSelect.CALCULATE3 -> state.calculate3
            PercentSelect.CALCULATE4 -> state.calculate4
        }
    val (v1Color, v2Color) =
        when (calculate.select) {
            ValueSelect.V1 -> ColorSet.select to ColorSet.text
            ValueSelect.V2 -> ColorSet.text to ColorSet.select
        }
    Column(modifier = Modifier.height(height)) {
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
            onClick = {
                v1Focus.requestFocus()
            },
            text = "V1",
            style = TextSet.extraBold.copy(color = v1Color, 24.sp),
        )
        ViewButtonValue(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(2.dp),
            onClick = {
                v2Focus.requestFocus()
            },
            text = "V2",
            style = TextSet.extraBold.copy(color = v2Color, 24.sp),
        )
    }
}
