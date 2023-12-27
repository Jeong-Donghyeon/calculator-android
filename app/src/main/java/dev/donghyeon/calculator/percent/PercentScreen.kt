package dev.donghyeon.calculator.percent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.theme.Black
import dev.donghyeon.calculator.theme.TsExtraBold
import dev.donghyeon.calculator.theme.White
import dev.donghyeon.calculator.view.BottonView

@Preview
@Composable
fun Preview_PercentScreen() =
    PercentScreen(
        state =
            PercentData(
                value1 = "100",
                value2 = "50",
                valueFlag = false,
                result = "50",
            ),
    )

@Composable
fun PercentScreen() {
    val viewModel: PercentViewModel = hiltViewModel()
    val state by viewModel.percentState.collectAsState()
    PercentScreen(
        state = state,
        action = viewModel,
    )
}

@Composable
fun PercentScreen(
    state: PercentData,
    action: PercentAction? = null,
) = Column(
    modifier =
        Modifier
            .background(Black)
            .fillMaxSize()
            .padding(10.dp)
            .padding(bottom = 20.dp),
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row {
            Text(
                text = "전체값  ",
                style = TsExtraBold.copy(White, 16.sp),
            )
            Text(
                text = state.value1,
                style = TsExtraBold.copy(White, 16.sp),
            )
            Text(
                text = "  의  ",
                style = TsExtraBold.copy(White, 16.sp),
            )
            Text(
                text = state.value2,
                style = TsExtraBold.copy(White, 16.sp),
            )
            Text(
                text = " % 는  얼마?",
                style = TsExtraBold.copy(White, 16.sp),
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = state.result,
            style = TsExtraBold.copy(White, 20.sp),
        )
    }
    Row {
        Column(modifier = Modifier.weight(3f).height(350.dp)) {
            Row(modifier = Modifier.weight(1f)) {
                BottonView(
                    modifier = Modifier.weight(1f).fillMaxHeight().padding(2.dp),
                    onClick = { action?.input("c") },
                    text = "c",
                )
                Box(modifier = Modifier.weight(2f).fillMaxHeight())
            }
            listOf(
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf("c", "0", "c"),
            ).forEach {
                Row(modifier = Modifier.weight(1f)) {
                    it.forEach {
                        BottonView(
                            modifier = Modifier.weight(1f).fillMaxHeight().padding(2.dp),
                            onClick = { action?.input(it) },
                            text = it,
                        )
                    }
                }
            }
        }
        Column(modifier = Modifier.weight(1f).height(350.dp)) {
            BottonView(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(2.dp),
                onClick = { action?.input("<") },
                text = "<",
            )
            BottonView(
                modifier = Modifier.fillMaxWidth().weight(2f).padding(2.dp),
                onClick = { action?.input("value1") },
                text = "값1",
            )
            BottonView(
                modifier = Modifier.fillMaxWidth().weight(2f).padding(2.dp),
                onClick = { action?.input("value2") },
                text = "값2",
            )
        }
    }
}
