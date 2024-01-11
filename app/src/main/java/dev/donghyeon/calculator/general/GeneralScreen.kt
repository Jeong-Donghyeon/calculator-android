package dev.donghyeon.calculator.general

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.R
import dev.donghyeon.calculator.common.KEY_HEIGHT
import dev.donghyeon.calculator.common.LocalViewModel
import dev.donghyeon.calculator.common.SideEffect
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.view.FontSizeRange
import dev.donghyeon.calculator.view.TitleView
import dev.donghyeon.calculator.view.ViewButtonKey
import dev.donghyeon.calculator.view.ViewFieldGeneral
import dev.donghyeon.calculator.view.ViewScrollTab
import dev.donghyeon.calculator.view.ViewTextResult
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_GeneralScreen() {
    GeneralScreen(
        state = GeneralState(),
    )
}

@Composable
fun GeneralScreen() {
    val viewModel: GeneralViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focus.requestFocus()
        viewModel.sideEffect.collectLatest {
            if (it is SideEffect.Toast) main.showToast(it.message)
        }
    }
    GeneralScreen(
        state = state,
        action = viewModel,
        menu = { main.openMenu() },
        focus = focus,
    )
}

@Composable
private fun GeneralScreen(
    state: GeneralState,
    action: GeneralAction? = null,
    menu: (() -> Unit)? = null,
    focus: FocusRequester? = null,
) {
    Column(modifier = Modifier.background(ColorSet.container)) {
        TitleView(title = Destination.General.route)
        Box(modifier = Modifier.weight(1f)) {
            CalculateView(
                state = state,
                focus = focus,
            )
        }
        MenuView(
            state = state,
            action = action,
            menu = menu,
        )
        KeyPadView(
            action = action,
        )
    }
}

@Composable
private fun CalculateView(
    state: GeneralState,
    focus: FocusRequester? = null,
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
            ViewFieldGeneral(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .focusRequester(focus ?: FocusRequester()),
                value = calculate.v,
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
private fun MenuView(
    state: GeneralState,
    action: GeneralAction? = null,
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
}

@Composable
private fun KeyPadView(action: GeneralAction? = null) {
    val keyList =
        listOf(
            listOf(
                GeneralKey.CLEAR,
                GeneralKey.SQUARE,
                GeneralKey.SEVEN,
                GeneralKey.FOUR,
                GeneralKey.ONE,
                GeneralKey.ZERO_ZERO,
            ),
            listOf(
                GeneralKey.LEFT,
                GeneralKey.OPEN,
                GeneralKey.EIGHT,
                GeneralKey.FIVE,
                GeneralKey.TWO,
                GeneralKey.ZERO,
            ),
            listOf(
                GeneralKey.RIGHT,
                GeneralKey.CLOSE,
                GeneralKey.NINE,
                GeneralKey.SIX,
                GeneralKey.THREE,
                GeneralKey.DECIMAL,
            ),
            listOf(
                GeneralKey.BACK,
                GeneralKey.DIVIDE,
                GeneralKey.MULTIPLY,
                GeneralKey.MINUS,
                GeneralKey.PLUS,
                GeneralKey.RESULT,
            ),
        )
    val height = keyList.first().count() * KEY_HEIGHT
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
                    ViewButtonKey(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(2.dp),
                        text = key.value,
                        onClick = { action?.inputKey(key) },
                    )
                }
            }
        }
    }
}
