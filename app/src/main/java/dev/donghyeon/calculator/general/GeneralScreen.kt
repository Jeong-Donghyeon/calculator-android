package dev.donghyeon.calculator.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_GeneralScreen() =
    GeneralScreen(
        state =
            GeneralData(
                calculation = "1+1",
                result = "2",
            ),
    )

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
) {
    Column(
        modifier =
        Modifier
            .background(ColorSet.container)
            .fillMaxSize()
            .padding(10.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Column(
            modifier =
            Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
        ) {
            Box(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = state.calculation,
                style = TextSet.extraBold.copy(ColorSet.text,30.sp),
            )
            Box(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                text = state.result,
                style = TextSet.extraBold.copy(ColorSet.text,30.sp),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
            ) { Text(text = "") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
            ) { Text(text = "") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
            ) { Text(text = "") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
            ) { Text(text = "←") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
            ) { Text(text = "C") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
            ) { Text(text = "") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("%") },
            ) { Text(text = "%") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("÷") },
            ) { Text(text = "÷") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("7") },
            ) { Text(text = "7") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("8") },
            ) { Text(text = "8") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("9") },
            ) { Text(text = "9") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("x") },
            ) { Text(text = "x") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("4") },
            ) { Text(text = "4") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("5") },
            ) { Text(text = "5") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("6") },
            ) { Text(text = "6") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("-") },
            ) { Text(text = "-") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("1") },
            ) { Text(text = "1") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("2") },
            ) { Text(text = "2") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("3") },
            ) { Text(text = "3") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("+") },
            ) { Text(text = "+") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("00") },
            ) { Text(text = "00") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input("0") },
            ) { Text(text = "0") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = { action?.input(".") },
            ) { Text(text = ".") }
            Button(
                modifier =
                Modifier
                    .weight(1f)
                    .height(60.dp),
                onClick = {},
            ) { Text(text = "=") }
        }
    }
}
