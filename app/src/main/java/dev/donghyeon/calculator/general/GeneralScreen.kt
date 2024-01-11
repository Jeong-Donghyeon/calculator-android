package dev.donghyeon.calculator.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
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
import dev.donghyeon.calculator.view.FontSizeRange
import dev.donghyeon.calculator.view.TitleView
import dev.donghyeon.calculator.view.ViewButtonNumber
import dev.donghyeon.calculator.view.ViewScrollTab
import dev.donghyeon.calculator.view.ViewTextField
import dev.donghyeon.calculator.view.ViewTextResult
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun Preview_GeneralScreen() = GeneralScreen(state = GeneralData())

@Composable
fun GeneralScreen() {
    val viewModel: GeneralViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    LaunchedEffect(true) {
        viewModel.sideEffect.collectLatest {
            if (it is SideEffect.Toast) main.showToast(it.message)
        }
    }
    GeneralScreen(
        state = state,
        action = viewModel,
        menu = { main.openMenu() },
    )
}

@Composable
private fun GeneralScreen(
    state: GeneralData,
    action: GeneralAction? = null,
    menu: (() -> Unit)? = null,
) {
    val keyPadHeight = (BUTTON_HEIGHT * 6).dp
    val focus = remember { FocusRequester() }
    Column(modifier = Modifier.background(ColorSet.container)) {
        TitleView(title = Destination.General.route)
        Box(modifier = Modifier.weight(1f)) {
            CalculateView(
                state = state,
                action = action,
                focus = focus,
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
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp),
                tabs = GeneralSelect.entries.map { it.value },
                index = state.select.ordinal,
                onTab = {
                    when (it) {
                        0 -> action?.inputGeneralSelect(GeneralSelect.CALCULATE1)
                        1 -> action?.inputGeneralSelect(GeneralSelect.CALCULATE2)
                        2 -> action?.inputGeneralSelect(GeneralSelect.CALCULATE3)
                        3 -> action?.inputGeneralSelect(GeneralSelect.CALCULATE4)
                    }
                },
            )
        }
        Column(
            modifier =
                Modifier
                    .padding(10.dp)
                    .padding(bottom = 10.dp),
        ) {
            KeyPadView(
                action = action,
                height = keyPadHeight,
            )
        }
    }
    LaunchedEffect(true) {
        focus.requestFocus()
    }
}

@Composable
private fun CalculateView(
    state: GeneralData,
    action: GeneralAction? = null,
    focus: FocusRequester,
) {
    val calculate =
        when (state.select) {
            GeneralSelect.CALCULATE1 -> state.calculate1
            GeneralSelect.CALCULATE2 -> state.calculate2
            GeneralSelect.CALCULATE3 -> state.calculate3
            GeneralSelect.CALCULATE4 -> state.calculate4
        }
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
            ViewTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .focusRequester(focus),
                value = calculate.v,
                color = ColorSet.text,
                line = false,
                onValueChange = {},
            )
        }
        ViewTextResult(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 26.dp),
            text = calculate.result,
            fontSizeRange =
                FontSizeRange(
                    min = 1.sp,
                    max = 34.sp,
                ),
        )
    }
}

@Composable
private fun KeyPadView(
    action: GeneralAction? = null,
    height: Dp,
) {
    Column(modifier = Modifier.height(height)) {
        listOf(
            listOf(
                GeneralKeyPad.CLEAR,
                GeneralKeyPad.LEFT,
                GeneralKeyPad.RIGHT,
                GeneralKeyPad.BACK,
            ),
            listOf(
                GeneralKeyPad.SQUARE,
                GeneralKeyPad.OPEN,
                GeneralKeyPad.CLOSE,
                GeneralKeyPad.DIVIDE,
            ),
            listOf(
                GeneralKeyPad.SEVEN,
                GeneralKeyPad.EIGHT,
                GeneralKeyPad.NINE,
                GeneralKeyPad.MULTIPLY,
            ),
            listOf(
                GeneralKeyPad.FOUR,
                GeneralKeyPad.FIVE,
                GeneralKeyPad.SIX,
                GeneralKeyPad.MINUS,
            ),
            listOf(
                GeneralKeyPad.ONE,
                GeneralKeyPad.TWO,
                GeneralKeyPad.THREE,
                GeneralKeyPad.PLUS,
            ),
            listOf(
                GeneralKeyPad.ZERO_ZERO,
                GeneralKeyPad.ZERO,
                GeneralKeyPad.DECIMAL,
                GeneralKeyPad.RESULT,
            ),
        ).forEach {
            Row(modifier = Modifier.weight(1f)) {
                it.forEach {
                    ViewButtonNumber(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                        onClick = { action?.inputKeyPad(it) },
                        text = it.value,
                    )
                }
            }
        }
    }
}
