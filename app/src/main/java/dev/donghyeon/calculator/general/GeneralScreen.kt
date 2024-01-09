package dev.donghyeon.calculator.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.view.TitleView
import dev.donghyeon.calculator.view.ViewButtonNumber

@Preview
@Composable
fun Preview_GeneralScreen() = GeneralScreen(state = GeneralData())

@Composable
fun GeneralScreen() {
    val viewModel: GeneralViewModel = hiltViewModel()
    val state by viewModel.generalState.collectAsState()
    GeneralScreen(
        state = state,
        action = viewModel,
    )
}

@Composable
fun GeneralScreen(
    state: GeneralData,
    action: GeneralAction? = null,
    menu: (() -> Unit)? = null,
) {
    Column(modifier = Modifier.background(ColorSet.container)) {
        TitleView(title = "일반 계산기")
        Box(modifier = Modifier.weight(1f)) {}
        Row(verticalAlignment = Alignment.Bottom) {
            ViewButtonNumber(
                modifier = Modifier.padding(start = 10.dp),
                text = "메뉴",
                height = 40.dp,
                size = 20.sp,
                onClick = menu ?: {},
            )
        }
        Column(
            modifier =
                Modifier
                    .padding(10.dp)
                    .padding(bottom = 10.dp),
        ) {
            Keyboard()
        }
    }
}

@Composable
private fun Keyboard() {
    Column(modifier = Modifier.height(420.dp)) {
        listOf(
            listOf("C", "<-", "->", "⌫"),
            listOf("^", "(", ")", "/"),
            listOf("7", "8", "9", "x"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("00", "0", ".", "="),
        ).forEach {
            Row(modifier = Modifier.weight(1f)) {
                it.forEach {
                    ViewButtonNumber(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(2.dp),
                        onClick = {},
                        text = it,
                    )
                }
            }
        }
    }
}
