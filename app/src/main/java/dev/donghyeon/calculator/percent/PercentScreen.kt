package dev.donghyeon.calculator.percent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.Navigation
import dev.donghyeon.calculator.R
import dev.donghyeon.calculator.calculate.PercentCalculateType
import dev.donghyeon.calculator.calculate.PercentUnit
import dev.donghyeon.calculator.common.InputKeyHeight
import dev.donghyeon.calculator.common.LocalViewModel
import dev.donghyeon.calculator.common.SideEffect
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet
import dev.donghyeon.calculator.view.FontSizeRange
import dev.donghyeon.calculator.view.TitleView
import dev.donghyeon.calculator.view.ViewButtonKey
import dev.donghyeon.calculator.view.ViewButtonKeyValue
import dev.donghyeon.calculator.view.ViewFieldNumber
import dev.donghyeon.calculator.view.ViewScrollTab
import dev.donghyeon.calculator.view.ViewTextResult
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_PercentScreen() {
    PercentScreen(
        state = PercentState(),
    )
}

@Composable
fun PercentScreen() {
    val viewModel: PercentViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    val v1Focus = remember { FocusRequester() }
    val v2Focus = remember { FocusRequester() }
    BackHandler { main.navigation(Navigation.Pop) }
    LaunchedEffect(Unit) {
        v1Focus.requestFocus()
        viewModel.sideEffect.collectLatest {
            when (it) {
                is SideEffect.Toast -> main.showToast(it.message)
                is SideEffect.Focus ->
                    when (it.fieldName) {
                        PercentKey.VALUE1.value -> v1Focus.requestFocus()
                        PercentKey.VALUE2.value -> v2Focus.requestFocus()
                    }
            }
        }
    }
    PercentScreen(
        state = state,
        action = viewModel,
        navInfo = { main.navigation(Navigation.Push(it)) },
        menu = { main.openMenu() },
        v1Focus = v1Focus,
        v2Focus = v2Focus,
    )
}

@Composable
private fun PercentScreen(
    state: PercentState,
    action: PercentAction? = null,
    navInfo: ((Destination) -> Unit)? = null,
    menu: (() -> Unit)? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    Column(modifier = Modifier.background(ColorSet.container)) {
        TitleView(
            title = Destination.PERCENT.route,
            navInfo = { navInfo?.invoke(it) },
        )
        Box(modifier = Modifier.weight(1f)) {
            CalculateView(
                state = state,
                action = action,
                v1Focus = v1Focus,
                v2Focus = v2Focus,
            )
        }
        MenuView(
            state = state,
            action = action,
            menu = menu,
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
private fun CalculateView(
    state: PercentState,
    action: PercentAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    val fieldTotalWith: Dp = 320.dp
    val fieldLeft: Dp = 50.dp
    val fieldRight: Dp = 60.dp
    val guideStrList =
        when (state.type) {
            PercentCalculateType.TYPE1 ->
                listOf(
                    "의",
                    "% 는",
                    "예) 100 의 10% 는 10",
                )
            PercentCalculateType.TYPE2 ->
                listOf(
                    "의",
                    "은",
                    "예) 100 의 10 은 10%",
                )
            PercentCalculateType.TYPE3 ->
                listOf(
                    "이/가",
                    "(으)로\n변하면",
                    "예) 100 이 10 으로 변하면 90% 감소",
                )
            PercentCalculateType.TYPE4 ->
                listOf(
                    "이/가",
                    "%\n증가하면",
                    "예) 100 이 10% 증가하면 110",
                )
        }
    val calculate =
        when (state.type) {
            PercentCalculateType.TYPE1 -> state.calculate1
            PercentCalculateType.TYPE2 -> state.calculate2
            PercentCalculateType.TYPE3 -> state.calculate3
            PercentCalculateType.TYPE4 -> state.calculate4
        }
    val (v1Color, v2Color) =
        when (calculate.select) {
            PercentValueSelect.VALUE1 -> ColorSet.select to ColorSet.text
            PercentValueSelect.VALUE2 -> ColorSet.text to ColorSet.select
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
                text = PercentKey.VALUE1.value,
                style = TextSet.extraBold.copy(v1Color, 24.sp),
                textAlign = TextAlign.Center,
            )
            ViewFieldNumber(
                modifier =
                    Modifier
                        .weight(1f)
                        .focusRequester(v1Focus ?: FocusRequester())
                        .onFocusChanged {
                            if (it.isFocused) {
                                action?.inputPercentValueSelect(PercentValueSelect.VALUE1)
                            }
                        },
                value = calculate.value1,
                color = v1Color,
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
            ViewFieldNumber(
                modifier =
                    Modifier
                        .weight(1f)
                        .focusRequester(v2Focus ?: FocusRequester())
                        .onFocusChanged {
                            if (it.isFocused) {
                                action?.inputPercentValueSelect(PercentValueSelect.VALUE2)
                            }
                        },
                value = calculate.value2,
                color = v2Color,
            )
            val v2FontSize =
                when (state.type) {
                    PercentCalculateType.TYPE3 -> 14.sp
                    PercentCalculateType.TYPE4 -> 14.sp
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
}

@Composable
private fun MenuView(
    state: PercentState,
    action: PercentAction? = null,
    menu: (() -> Unit)? = null,
) {
    Row {
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(ColorSet.button),
            onClick = menu ?: {},
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_menu_24px),
                tint = ColorSet.text,
                contentDescription = "Menu",
            )
        }
        ViewScrollTab(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
            tabs = PercentCalculateType.entries.map { it.value },
            index = state.type.ordinal,
            onTab = {
                when (it) {
                    0 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE1)
                    1 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE2)
                    2 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE3)
                    3 -> action?.inputPercentCalculateType(PercentCalculateType.TYPE4)
                }
            },
        )
    }
}

@Composable
private fun KeyView(
    state: PercentState,
    action: PercentAction? = null,
    v1Focus: FocusRequester? = null,
    v2Focus: FocusRequester? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val keyList =
        listOf(
            listOf(
                PercentKey.CLEAR,
                PercentKey.SEVEN,
                PercentKey.FOUR,
                PercentKey.ONE,
                PercentKey.COPY,
            ),
            listOf(
                PercentKey.LEFT,
                PercentKey.EIGHT,
                PercentKey.FIVE,
                PercentKey.TWO,
                PercentKey.ZERO,
            ),
            listOf(
                PercentKey.RIGHT,
                PercentKey.NINE,
                PercentKey.SIX,
                PercentKey.THREE,
                PercentKey.DECIMAL,
            ),
            listOf(
                PercentKey.BACK,
                PercentKey.VALUE1,
                PercentKey.VALUE2,
            ),
        )
    val height = keyList.first().count() * InputKeyHeight.value
    val calculate =
        when (state.type) {
            PercentCalculateType.TYPE1 -> state.calculate1
            PercentCalculateType.TYPE2 -> state.calculate2
            PercentCalculateType.TYPE3 -> state.calculate3
            PercentCalculateType.TYPE4 -> state.calculate4
        }
    Row(
        modifier =
            Modifier
                .padding(10.dp)
                .padding(bottom = 20.dp)
                .height(height.dp),
    ) {
        keyList.forEach { row ->
            Column(modifier = Modifier.weight(1f)) {
                row.forEach { key ->
                    when (key) {
                        PercentKey.VALUE1, PercentKey.VALUE2 -> {
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
                                        PercentKey.VALUE1 -> v1Focus?.requestFocus()
                                        PercentKey.VALUE2 -> v2Focus?.requestFocus()
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
                                onClick = {
                                    if (key == PercentKey.COPY) {
                                        val copyStr =
                                            calculate.result
                                                .replace(",", "")
                                                .replace(PercentUnit.PERCENT.value, "")
                                                .replace(PercentUnit.UP.value, "")
                                                .replace(PercentUnit.DOWN.value, "")
                                                .trim()
                                        clipboardManager.setText(
                                            AnnotatedString(copyStr),
                                        )
                                    }
                                    action?.inputKey(key)
                                },
                            )
                    }
                }
            }
        }
    }
}
