package dev.donghyeon.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Preview_GeneralCalculatorScreen() = GeneralCalculatorScreen()

@Composable
fun GeneralCalculatorScreen() = Column(
    modifier = Modifier
        .fillMaxSize().padding(10.dp)
        .padding(bottom = 20.dp),
    verticalArrangement = Arrangement.spacedBy(5.dp)
) {
    Column(modifier = Modifier.fillMaxWidth().weight(1f)) {

    }
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "메뉴") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "로그") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "^") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "←") }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "C") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "( )") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "%") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "÷") }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "7") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "8") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "9") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "x") }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "4") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "5") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "6") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "-") }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "1") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "2") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "3") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "+") }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "00") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "0") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = ".") }
        Button(modifier = Modifier.weight(1f).height(60.dp), onClick = {}) { Text(text = "=") }
    }
}
