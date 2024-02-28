package com.donghyeon.dev.calculator.ratio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.RatioType
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewButtonKeyValue
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.donghyeon.dev.calculator.view.ViewTitle

@Preview
@Composable
private fun Preview_RatioScreen() {
    RatioScreen(
        state = RatioState(),
    )
}

@Composable
fun RatioScreen() {
    val viewModel: RatioViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    RatioScreen(
        state = state,
        action = viewModel,
    )
}

@Composable
private fun RatioScreen(
    state: RatioState,
    action: RatioAction? = null,
    navDest: ((Dest) -> Unit)? = null,
) {
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ViewTitle(
            title = stringResource(id = Dest.RATIO.title),
            navDest = { navDest?.invoke(it) },
        )
        Spacer(modifier = Modifier.weight(1f))
        ViewScrollTab(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 3.dp),
            tabs = stringArrayResource(id = R.array.ratio_type).toList(),
            index = state.type.index,
            onTab = { action?.inputType(it) },
        )
        KeyView(
            state = state,
            action = action,
        )
    }
}

@Composable
private fun KeyView(
    state: RatioState,
    action: RatioAction? = null,
) {
    val keyList1 =
        listOf(
            RatioKey.Clear,
            RatioKey.Left,
            RatioKey.Right,
            RatioKey.Backspace,
        )
    val keyList2 =
        listOf(
            listOf(
                RatioKey.Seven,
                RatioKey.Eight,
                RatioKey.Nine,
            ),
            listOf(
                RatioKey.Four,
                RatioKey.Five,
                RatioKey.Six,
            ),
            listOf(
                RatioKey.One,
                RatioKey.Two,
                RatioKey.Three,
            ),
            listOf(
                RatioKey.ZeroZero,
                RatioKey.Zero,
                RatioKey.Decimal,
            ),
        )
    val keyList3 =
        if (state.type == RatioType.RATIO) {
            listOf(
                RatioKey.Value1,
                RatioKey.Value2,
                RatioKey.Value3,
            )
        } else {
            listOf(
                RatioKey.Value1,
                RatioKey.Value2,
            )
        }
    val viewButtonKey: @Composable RowScope.(RatioKey) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .weight(1f)
                    .height(InputKeyHeight.value.dp)
                    .padding(2.dp),
            text = it.value,
            icon =
                when (it) {
                    is RatioKey.Left -> it.value.toInt() to 32.dp
                    is RatioKey.Right -> it.value.toInt() to 32.dp
                    is RatioKey.Backspace -> it.value.toInt() to 32.dp
                    else -> null
                },
            onClick = { action?.inputKey(it) },
        )
    }
    Column(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 3.dp),
    ) {
        Row {
            keyList1.forEach {
                viewButtonKey(it)
            }
        }
        val height = InputKeyHeight.value * keyList2.count()
        Row(modifier = Modifier.height(height.dp)) {
            Column(modifier = Modifier.weight(3f)) {
                keyList2.forEach { keyList ->
                    Row {
                        keyList.forEach {
                            viewButtonKey(it)
                        }
                    }
                }
            }
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                keyList3.forEach {
                    ViewButtonKeyValue(
                        modifier = Modifier.fillMaxWidth().weight(1f).padding(2.dp),
                        text = it.value,
                        color = ColorSet.text,
                    )
                }
            }
        }
    }
}
