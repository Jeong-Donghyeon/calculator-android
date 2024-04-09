package com.donghyeon.dev.calculator.date

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donghyeon.dev.calculator.MainAction
import com.donghyeon.dev.calculator.calculate.DateType
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.view.ViewButtonKey

@Preview
@Composable
private fun Preview_DateScreen() =
    DateScreen(
        state = DateState(),
    )

@Composable
fun DateScreen(
    state: DateState,
    action: DateAction? = null,
    mainAction: MainAction? = null,
) {
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        KeyView(
            state = state,
            action = action,
        )
    }
}

@Composable
private fun KeyView(
    state: DateState,
    action: DateAction? = null,
) {
    val keyList1 =
        listOf(
            DateKey.Clear,
            DateKey.Left,
            DateKey.Right,
            DateKey.Backspace,
        )
    val keyList2 =
        listOf(
            listOf(
                DateKey.Seven,
                DateKey.Eight,
                DateKey.Nine,
            ),
            listOf(
                DateKey.Four,
                DateKey.Five,
                DateKey.Six,
            ),
            listOf(
                DateKey.One,
                DateKey.Two,
                DateKey.Three,
            ),
            listOf(
                DateKey.ZeroZero,
                DateKey.Zero,
                DateKey.Decimal,
            ),
        )
    val keyList3 =
        when (state.type) {
            DateType.TIME_COMVERT ->
                listOf(
                    DateKey.Past,
                    DateKey.Next,
                )
            else ->
                listOf(
                    DateKey.Past,
                    DateKey.Copy,
                    DateKey.Next,
                )
        }
    val viewButtonKey: @Composable RowScope.(DateKey) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .height(InputKeyHeight.value.dp),
            text = it.value,
            icon =
                when (it) {
                    is DateKey.Left -> it.value.toInt() to 32.dp
                    is DateKey.Right -> it.value.toInt() to 32.dp
                    is DateKey.Backspace -> it.value.toInt() to 32.dp
                    is DateKey.Past -> it.value.toInt() to 32.dp
                    is DateKey.Copy -> it.value.toInt() to 32.dp
                    is DateKey.Next -> it.value.toInt() to 32.dp
                    else -> null
                },
            onClick = {},
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
        Row(modifier = Modifier.height(IntrinsicSize.Max)) {
            Column(modifier = Modifier.weight(3f)) {
                keyList2.forEach {
                    Row {
                        it.forEach {
                            viewButtonKey(it)
                        }
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                val weightList =
                    if (state.type == DateType.TIME_COMVERT) {
                        listOf(1f, 3f)
                    } else {
                        listOf(1f, 1f, 2f)
                    }
                keyList3.forEachIndexed { i, it ->
                    ViewButtonKey(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .weight(weightList[i]),
                        text = it.value,
                        icon =
                            when (it) {
                                is DateKey.Past -> it.value.toInt() to 28.dp
                                is DateKey.Copy -> it.value.toInt() to 30.dp
                                is DateKey.Next -> it.value.toInt() to 36.dp
                                else -> null
                            },
                        onClick = {},
                    )
                }
            }
        }
    }
}
