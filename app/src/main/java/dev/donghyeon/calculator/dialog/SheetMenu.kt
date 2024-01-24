package dev.donghyeon.calculator.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import dev.donghyeon.calculator.Destination
import dev.donghyeon.calculator.theme.ColorSet
import dev.donghyeon.calculator.theme.TextSet

@Composable
fun SheetMenu(
    close: () -> Unit,
    nav: (Destination) -> Unit,
) {
    val calculatorList =
        listOf(
            Destination.GENERAL,
            Destination.PERCENT,
            Destination.CONVERT,
        )
    BottomSheetDialog(
        onDismissRequest = close,
        properties = BottomSheetDialogProperties(),
        content = {
            Box(
                modifier =
                    Modifier
                        .padding(horizontal = 7.dp)
                        .padding(bottom = 20.dp)
                        .clip(RoundedCornerShape(10.dp)),
            ) {
                Column(modifier = Modifier.background(ColorSet.button)) {
                    calculatorList.forEach {
                        Text(
                            modifier =
                                Modifier
                                    .clickable { nav(it) }
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
                            text = it.route,
                            style = TextSet.bold.copy(ColorSet.text, 18.sp),
                            textAlign = TextAlign.Center,
                        )
                        Box(
                            modifier =
                                Modifier
                                    .padding(horizontal = 7.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(ColorSet.text.copy(alpha = 0.1f)),
                        )
                    }
                    Text(
                        modifier =
                            Modifier
                                .background(
                                    color = ColorSet.button,
                                    shape = RoundedCornerShape(5.dp),
                                )
                                .clickable { close() }
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                        text = "닫기",
                        style = TextSet.bold.copy(ColorSet.text, 18.sp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
    )
}
