package dev.donghyeon.calculator.convert

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.Navigation
import dev.donghyeon.calculator.R
import dev.donghyeon.calculator.common.LocalViewModel
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.view.TitleView

@Preview
@Composable
private fun Preview_ConvertScreen() {
    ConvertScreen(
        state = ConvertState(),
    )
}

@Composable
fun ConvertScreen() {
    val viewModel: ConvertViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    ConvertScreen(
        state = state,
        action = viewModel,
        navInfo = { main.navigation(Navigation.Push(it)) },
        menu = { main.openMenu() },
    )
}

@Composable
private fun ConvertScreen(
    state: ConvertState,
    action: ConvertAction? = null,
    navInfo: ((Destination) -> Unit)? = null,
    menu: (() -> Unit)? = null,
) {
    Column(modifier = Modifier.background(ColorSet.container).fillMaxSize()) {
        TitleView(
            title = Destination.CONVERT.route,
            navInfo = { navInfo?.invoke(it) },
        )
        Box(modifier = Modifier.weight(1f))
        MenuView(
            state = state,
            action = action,
            menu = menu,
        )
        Box(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MenuView(
    state: ConvertState,
    action: ConvertAction? = null,
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
                contentDescription = "Menu",
            )
        }
    }
}
