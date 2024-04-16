package com.donghyeon.dev.calculator.convert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.MainAction
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.ConvertType
import com.donghyeon.dev.calculator.calculate.unitAreaList
import com.donghyeon.dev.calculator.calculate.unitLengthList
import com.donghyeon.dev.calculator.calculate.unitSppedList
import com.donghyeon.dev.calculator.calculate.unitTimeList
import com.donghyeon.dev.calculator.calculate.unitVolumeList
import com.donghyeon.dev.calculator.calculate.unitWeightList
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_ConvertScreen_Null() =
    ConvertScreen(
        state =
            ConvertState(
                type = null,
            ),
    )

@Preview
@Composable
private fun Preview_ConvertScreen() =
    ConvertScreen(
        state =
            ConvertState(
                type = ConvertType.LENGTH,
            ),
    )

@Preview
@Composable
fun Preview_SheetUnit() =
    SheetUnit(
        unitList = unitLengthList,
    )

@Composable
fun ConvertScreen(
    state: ConvertState,
    action: ConvertAction? = null,
    mainAction: MainAction? = null,
) {
    val context = LocalContext.current
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focus.requestFocus()
        action?.sideEffect?.collectLatest {
            when (it) {
                is SideEffect.Toast -> mainAction?.showToast(context.getString(it.id))
                else -> {}
            }
        }
    }
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.type?.let { type ->
            Spacer(modifier = Modifier.weight(1f))
            repeat(4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val modifier =
                        if (it == 0) {
                            Modifier
                                .padding(start = 50.dp)
                                .width(200.dp)
                                .focusRequester(focus)
                        } else {
                            Modifier
                                .padding(start = 50.dp)
                                .width(200.dp)
                        }
                    ViewFieldNumber(
                        modifier = modifier,
                        value = state.unitValue,
                        color =
                            if (it == 0) {
                                ColorSet.select
                            } else {
                                ColorSet.text
                            },
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        modifier =
                            Modifier
                                .padding(bottom = 10.dp)
                                .width(50.dp),
                        text = state.unit,
                        style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
                    )
                }
                if (it == 3) {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Spacer(modifier = Modifier.weight(1.3f))
            ViewScrollTab(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(bottom = 3.dp),
                tabs = stringArrayResource(id = R.array.convert_type).toList(),
                index = type.ordinal,
                onTab = { action?.inputType(it) },
            )
        } ?: Spacer(modifier = Modifier.weight(1f))
        ViewConvertKey {
            when (it) {
                is ConvertKey.Unit -> action?.sheet(true)
                else -> {}
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
    }
    if (state.sheet) {
        state.type?.let {
            SheetUnit(
                unitList =
                    when (it) {
                        ConvertType.LENGTH -> unitLengthList
                        ConvertType.AREA -> unitAreaList
                        ConvertType.VOLUME -> unitVolumeList
                        ConvertType.WEIGHT -> unitWeightList
                        ConvertType.SPEED -> unitSppedList
                        ConvertType.TIME -> unitTimeList
                    },
                select = { action?.sheet(false) },
                close = { action?.sheet(false) },
            )
        }
    }
}

@Composable
private fun ViewConvertKey(input: (ConvertKey) -> Unit = {}) {
    val keyList1 =
        listOf(
            ConvertKey.Clear,
            ConvertKey.Left,
            ConvertKey.Right,
            ConvertKey.Backspace,
        )
    val keyList2 =
        listOf(
            listOf(
                ConvertKey.Seven,
                ConvertKey.Eight,
                ConvertKey.Nine,
            ),
            listOf(
                ConvertKey.Four,
                ConvertKey.Five,
                ConvertKey.Six,
            ),
            listOf(
                ConvertKey.One,
                ConvertKey.Two,
                ConvertKey.Three,
            ),
            listOf(
                ConvertKey.ZeroZero,
                ConvertKey.Zero,
                ConvertKey.Decimal,
            ),
        )
    val keyList3 =
        listOf(
            ConvertKey.Copy,
            ConvertKey.Paste(""),
            ConvertKey.Enter,
            ConvertKey.Unit,
        )
    val viewButtonKey: @Composable RowScope.(ConvertKey) -> Unit = {
        ViewButtonKey(
            modifier =
                Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .height(InputKeyHeight.value.dp),
            text = it.value,
            icon =
                when (it) {
                    is ConvertKey.Left -> it.value.toInt() to 32.dp
                    is ConvertKey.Right -> it.value.toInt() to 32.dp
                    is ConvertKey.Backspace -> it.value.toInt() to 32.dp
                    else -> null
                },
            onClick = { input(it) },
        )
    }
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
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
                keyList3.forEach { key ->
                    ViewButtonKey(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .fillMaxWidth()
                                .weight(1f),
                        text = key.value,
                        icon =
                            when (key) {
                                is ConvertKey.Paste -> key.value.toInt() to 28.dp
                                is ConvertKey.Copy -> key.value.toInt() to 30.dp
                                is ConvertKey.Unit -> key.value.toInt() to 36.dp
                                is ConvertKey.Enter -> key.value.toInt() to 36.dp
                                else -> null
                            },
                        onClick = { input(key) },
                    )
                }
            }
        }
    }
}

@Composable
fun SheetUnit(
    unitList: List<String>,
    select: (String) -> Unit = {},
    close: () -> Unit = {},
) {
    BottomSheetDialog(
        onDismissRequest = close,
        properties =
            BottomSheetDialogProperties(
                behaviorProperties =
                    BottomSheetBehaviorProperties(
                        isDraggable = false,
                        peekHeight = BottomSheetBehaviorProperties.PeekHeight(Int.MAX_VALUE),
                    ),
            ),
        content = {
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 7.dp)
                        .padding(bottom = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ColorSet.button)
                        .height(460.dp)
                        .padding(top = 7.dp),
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                    ) {
                        unitList.subList(0, 4).forEach {
                            Text(
                                modifier =
                                    Modifier
                                        .clickable { select(it) }
                                        .fillMaxWidth()
                                        .padding(vertical = 18.dp),
                                text = it,
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
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                    Box(
                        modifier =
                            Modifier
                                .padding(vertical = 7.dp)
                                .width(1.dp)
                                .height(400.dp)
                                .background(ColorSet.text.copy(alpha = 0.1f)),
                    )
                    Column(
                        modifier =
                            Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                    ) {
                        unitList.forEach {
                            Text(
                                modifier =
                                    Modifier
                                        .clickable { select(it) }
                                        .fillMaxWidth()
                                        .padding(vertical = 18.dp),
                                text = it,
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
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
                Box(
                    modifier =
                        Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ColorSet.text.copy(alpha = 0.1f)),
                )
                IconButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                    onClick = { close() },
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_close_24px),
                        tint = ColorSet.text,
                        contentDescription = "CloseIcon",
                    )
                }
            }
        },
    )
}
