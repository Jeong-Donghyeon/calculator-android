package com.donghyeon.dev.calculator.convert

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
import com.donghyeon.dev.calculator.Destination
import com.donghyeon.dev.calculator.Navigation
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.view.ViewTitle

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
        nav = { main.navigation(it) },
    )
}

@Composable
private fun ConvertScreen(
    state: ConvertState,
    action: ConvertAction? = null,
    nav: ((Navigation) -> Unit)? = null,
) {
    Column(modifier = Modifier.background(ColorSet.background).fillMaxSize()) {
        ViewTitle(
            title = Destination.CONVERT.route,
            nav = { nav?.invoke(it) },
        )
        Box(modifier = Modifier.weight(1f))
        MenuView(
            state = state,
            action = action,
        )
        Box(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MenuView(
    state: ConvertState,
    action: ConvertAction? = null,
) {
    Row {
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(
            modifier =
                Modifier
                    .clip(CircleShape)
                    .background(ColorSet.button),
            onClick = {},
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_menu_24px),
                tint = ColorSet.text,
                contentDescription = "Menu",
            )
        }
    }
}
