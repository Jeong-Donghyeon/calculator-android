package com.donghyeon.dev.calculator.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.common.InputKeyHeight

@Preview
@Composable
fun Preview_ViewKeyboard() = ViewKeyboard()

@Composable
fun ViewKeyboard(
    input: (Keyboard) -> Unit = {}
) {
    val keyList1 =
        listOf(
            Keyboard.Clear,
            Keyboard.Left,
            Keyboard.Right,
            Keyboard.Backspace,
        )
    val keyList2 =
        listOf(
            listOf(
                Keyboard.Seven,
                Keyboard.Eight,
                Keyboard.Nine,
            ),
            listOf(
                Keyboard.Four,
                Keyboard.Five,
                Keyboard.Six,
            ),
            listOf(
                Keyboard.One,
                Keyboard.Two,
                Keyboard.Three,
            ),
            listOf(
                Keyboard.ZeroZero,
                Keyboard.Zero,
                Keyboard.Decimal,
            ),
        )
    val keyList3 =
        listOf(
            Keyboard.Paste(""),
            Keyboard.Copy,
            Keyboard.Enter,
        )
    val viewButtonKey: @Composable RowScope.(Keyboard) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .height(InputKeyHeight.value.dp),
            text = it.value,
            icon =
            when (it) {
                is Keyboard.Left -> it.value.toInt() to 32.dp
                is Keyboard.Right -> it.value.toInt() to 32.dp
                is Keyboard.Backspace -> it.value.toInt() to 32.dp
                else -> null
            },
            onClick = { input(it) },
        )
    }
    Column {
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
                val weightList = listOf(1f, 1f, 2f)
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
                            is Keyboard.Paste -> it.value.toInt() to 28.dp
                            is Keyboard.Copy -> it.value.toInt() to 30.dp
                            is Keyboard.Enter -> it.value.toInt() to 36.dp
                            else -> null
                        },
                        onClick = {},
                    )
                }
            }
        }
    }
}


sealed class Keyboard(val value: String) {

    data object Clear : Keyboard("C")

    data object Left : Keyboard(R.drawable.ic_left_24px.toString())

    data object Right : Keyboard(R.drawable.ic_right_24px.toString())

    data object Backspace : Keyboard(R.drawable.ic_backspace_24px.toString())

    data object Copy : Keyboard(R.drawable.ic_copy_24px.toString())

    data class Paste(val result: String) : Keyboard(R.drawable.ic_paste_24px.toString())

    data object Enter : Keyboard(R.drawable.ic_tab_24px.toString())

    data object ZeroZero : Keyboard("00")

    data object Zero : Keyboard("0")

    data object Decimal : Keyboard(".")

    data object One : Keyboard("1")

    data object Two : Keyboard("2")

    data object Three : Keyboard("3")

    data object Four : Keyboard("4")

    data object Five : Keyboard("5")

    data object Six : Keyboard("6")

    data object Seven : Keyboard("7")

    data object Eight : Keyboard("8")

    data object Nine : Keyboard("9")
}
