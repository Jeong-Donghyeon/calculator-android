package com.donghyeon.dev.calculator.convert

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donghyeon.dev.calculator.Dest
import com.donghyeon.dev.calculator.Nav
import com.donghyeon.dev.calculator.R
import com.donghyeon.dev.calculator.calculate.ConvertType
import com.donghyeon.dev.calculator.calculate.unitAreaList
import com.donghyeon.dev.calculator.calculate.unitDataList
import com.donghyeon.dev.calculator.calculate.unitLengthList
import com.donghyeon.dev.calculator.calculate.unitSppedList
import com.donghyeon.dev.calculator.calculate.unitTimeList
import com.donghyeon.dev.calculator.calculate.unitVolumeList
import com.donghyeon.dev.calculator.calculate.unitWeightList
import com.donghyeon.dev.calculator.common.InputKeyHeight
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.SideEffect
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet
import com.donghyeon.dev.calculator.view.FontSizeRange
import com.donghyeon.dev.calculator.view.ViewButtonKey
import com.donghyeon.dev.calculator.view.ViewFieldNumber
import com.donghyeon.dev.calculator.view.ViewScrollTab
import com.donghyeon.dev.calculator.view.ViewTextResult
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
private fun Preview_ConvertScreen() {
    ConvertScreen(
        state = ConvertState(),
    )
}

@Preview
@Composable
fun Preview_SheetUnit() {
    SheetUnit(
        unitList = unitLengthList,
    )
}

@Composable
fun ConvertScreen() {
    val viewModel: ConvertViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val main = LocalViewModel.current
    val context = LocalContext.current
    BackHandler { main.navigation(Nav.POP, null) }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest {
            when (it) {
                is SideEffect.Toast -> main.showToast(context.getString(it.id))
                else -> {}
            }
        }
    }
    ConvertScreen(
        state = state,
        action = viewModel,
        navDest = { main.navigation(Nav.PUSH, it) },
    )
    if (state.sheet) {
        SheetUnit(
            unitList =
                when (state.type) {
                    ConvertType.LENGTH -> unitLengthList
                    ConvertType.AREA -> unitAreaList
                    ConvertType.VOLUME -> unitVolumeList
                    ConvertType.WEIGHT -> unitWeightList
                    ConvertType.SPEED -> unitSppedList
                    ConvertType.TIME -> unitTimeList
                    ConvertType.DATA -> unitDataList
                },
            select = { viewModel.sheet(false) },
            close = { viewModel.sheet(false) },
        )
    }
}

@Composable
fun ConvertScreen(
    state: ConvertState,
    action: ConvertAction? = null,
    navDest: ((Dest) -> Unit)? = null,
) {
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { focus.requestFocus() }
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.weight(1.5f))
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = ConvertKey.Unit.value,
                style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.width(10.dp))
            ViewFieldNumber(
                modifier =
                    Modifier
                        .width(200.dp)
                        .focusRequester(focus),
                value = state.unitValue,
                color = ColorSet.select,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.width(50.dp),
                text = state.unit,
                style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))
        state.resultList.forEachIndexed { i, it ->
            Row(
                modifier =
                    Modifier
                        .width(300.dp)
                        .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.width(45.dp),
                    text = "R${i + 1}",
                    style = TextSet.extraBold.copy(ColorSet.text, 24.sp),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.weight(1f))
                ViewTextResult(
                    text = state.resultValueList[i],
                    fontSizeRange =
                        FontSizeRange(
                            min = 1.sp,
                            max = 24.sp,
                        ),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier.width(50.dp),
                    text = it,
                    style = TextSet.extraBold.copy(ColorSet.text, 20.sp),
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.weight(1f))
        ViewScrollTab(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(bottom = 3.dp),
            tabs = stringArrayResource(id = R.array.convert_type).toList(),
            index = state.type.ordinal,
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
    state: ConvertState,
    action: ConvertAction? = null,
) {
    val keyList =
        listOf(
            listOf(
                ConvertKey.Clear,
                ConvertKey.Left,
                ConvertKey.Right,
                ConvertKey.Backspace,
            ),
            listOf(
                ConvertKey.Seven,
                ConvertKey.Eight,
                ConvertKey.Nine,
                ConvertKey.Unit,
            ),
            listOf(
                ConvertKey.Four,
                ConvertKey.Five,
                ConvertKey.Six,
                ConvertKey.Result1,
            ),
            listOf(
                ConvertKey.One,
                ConvertKey.Two,
                ConvertKey.Three,
                ConvertKey.Result2,
            ),
            listOf(
                ConvertKey.ZeroZero,
                ConvertKey.Zero,
                ConvertKey.Decimal,
                ConvertKey.Result3,
            ),
        )
    Column(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .padding(bottom = 3.dp),
    ) {
        keyList.forEach {
            Row {
                it.forEach {
                    ViewButtonKey(
                        modifier =
                            Modifier
                                .padding(2.dp)
                                .weight(1f)
                                .height(InputKeyHeight),
                        text = it.value,
                        icon =
                            when (it) {
                                is ConvertKey.Left -> it.value.toInt() to 32.dp
                                is ConvertKey.Right -> it.value.toInt() to 32.dp
                                is ConvertKey.Backspace -> it.value.toInt() to 32.dp
                                else -> null
                            },
                        onClick = {
                            when (it) {
                                ConvertKey.Unit,
                                ConvertKey.Result1,
                                ConvertKey.Result2,
                                ConvertKey.Result3,
                                -> action?.sheet(true)
                                else -> action?.inputKey(it)
                            }
                        },
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
                    unitList.forEach {
                        Text(
                            modifier =
                                Modifier
                                    .clickable { select(it) }
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
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
                        text = stringResource(id = R.string.close),
                        style = TextSet.bold.copy(ColorSet.text, 18.sp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
    )
}
