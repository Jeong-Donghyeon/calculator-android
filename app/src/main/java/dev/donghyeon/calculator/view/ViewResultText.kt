package dev.donghyeon.calculator.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewTextResult() =
    ViewTextResult(
        text = "999,999,999,999,999",
        fontSizeRange =
            FontSizeRange(
                min = 1.sp,
                max = 34.sp,
            ),
    )

@Composable
fun ViewTextResult(
    modifier: Modifier = Modifier,
    text: String,
    fontSizeRange: FontSizeRange,
) {
    var fontSizeValue by remember(text) { mutableFloatStateOf(fontSizeRange.max.value) }
    var readyToDraw by remember(text) { mutableStateOf(false) }
    Text(
        modifier = modifier.drawWithContent { if (readyToDraw) drawContent() },
        text = text,
        maxLines = 1,
        color = ColorSet.result,
        textAlign = TextAlign.Center,
        style = TextSet.extraBold,
        fontSize = fontSizeValue.sp,
        onTextLayout = {
            if (it.didOverflowHeight && !readyToDraw) {
                val nextFontSizeValue = fontSizeValue - fontSizeRange.step.value
                if (nextFontSizeValue <= fontSizeRange.min.value) {
                    fontSizeValue = fontSizeRange.min.value
                    readyToDraw = true
                } else {
                    fontSizeValue = nextFontSizeValue
                }
            } else {
                readyToDraw = true
            }
        },
    )
}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_STEP,
) {
    init {
        require(min < max) { "min should be less than max, $this" }
        require(step.value > 0) { "step should be greater than 0, $this" }
    }

    companion object {
        private val DEFAULT_TEXT_STEP = 1.sp
    }
}
