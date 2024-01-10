package dev.donghyeon.calculator.general

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.R
import dev.donghyeon.calculator.common.LocalViewModel
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.view.TitleView
import dev.donghyeon.calculator.view.ViewButtonNumber
import dev.donghyeon.calculator.view.ViewScrollTab

@Preview
@Composable
fun Preview_GeneralScreen() = GeneralScreen(state = GeneralData())

@Composable
fun GeneralScreen() {
    val viewModel: GeneralViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    GeneralScreen(
        state = state,
        action = viewModel,
        menu = { main.openMenu() },
    )
}

@Composable
fun GeneralScreen(
    state: GeneralData,
    action: GeneralAction? = null,
    menu: (() -> Unit)? = null,
) {
    Column(modifier = Modifier.background(ColorSet.container)) {
        TitleView(title = Destination.General.route)
        Box(modifier = Modifier.weight(1f)) {}
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
