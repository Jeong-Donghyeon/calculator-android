package com.donghyeon.dev.calculator.view

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.theme.TextSet

@Preview
@Composable
fun Preview_ViewScrollTab() {
    ViewScrollTab(
        modifier = Modifier.background(ColorSet.container),
        tabs = listOf("비율", "일부", "증값", "증율"),
    )
}

@Composable
fun ViewScrollTab(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    index: Int = 0,
    onTab: (Int) -> Unit = {},
) {
    CustomScrollableTabRow(
        modifier = modifier.then(Modifier.padding(horizontal = 5.dp)),
        backgroundColor = Color.Transparent,
        contentColor = ColorSet.text,
        edgePadding = 0.dp,
        divider = {},
        indicator = {
            TabRowDefaults.Indicator(
                modifier =
                    Modifier.customTabIndicatorOffset2(
                        currentTabPosition = it[index],
                    ),
                color = ColorSet.select,
            )
        },
        tabs = {
            tabs.forEachIndexed { tabIndex, tab ->
                val value = tabIndex == index
                CustomTabTransition(
                    selected = value,
                    content = {
                        Text(
                            modifier =
                                Modifier
                                    .padding(10.dp)
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = { onTab(tabIndex) })
                                    },
                            text = tab,
                            textAlign = TextAlign.Center,
                            style =
                                if (value) {
                                    TextSet.extraBold.copy(ColorSet.select, 20.sp)
                                } else {
                                    TextSet.bold.copy(ColorSet.text, 20.sp)
                                },
                        )
                    },
                )
            }
        },
    )
}

@Composable
@UiComposable
private fun CustomScrollableTabRow(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ColorSet.container,
    contentColor: Color = contentColorFor(backgroundColor),
    edgePadding: Dp = 52.dp,
    indicator: @Composable (tabPositions: List<TabPos>) -> Unit,
    divider: @Composable () -> Unit = @Composable { TabSlots.Divider },
    tabs: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        val scrollState = rememberScrollState()
        SubcomposeLayout(
            Modifier
                .wrapContentSize(align = Alignment.CenterStart)
                .horizontalScroll(scrollState)
                .selectableGroup()
                .clipToBounds(),
        ) { constraints ->
            val minTabWidth = 50.dp.roundToPx()
            val padding = edgePadding.roundToPx()
            val tabConstraints = constraints.copy(minWidth = minTabWidth)

            val tabPlaceables =
                subcompose(TabSlots.Tabs, tabs)
                    .map { it.measure(tabConstraints) }

            var layoutWidth = padding * 2
            var layoutHeight = 0
            tabPlaceables.forEach {
                layoutWidth += it.width
                layoutHeight = maxOf(layoutHeight, it.height)
            }

            // Position the children.
            layout(layoutWidth, layoutHeight) {
                // Place the tabs
                val tabPositions = mutableListOf<TabPos>()
                var left = padding
                tabPlaceables.forEach {
                    it.placeRelative(left, 0)
                    tabPositions.add(TabPos(left = left.toDp(), right = it.width.toDp() - left.toDp(), width = it.width.toDp()))
                    left += it.width
                }
                // The divider is measured with its own height, and width equal to the total width
                // of the tab row, and then placed on top of the tabs.
                subcompose(TabSlots.Divider, divider).forEach {
                    val placeable =
                        it.measure(
                            constraints.copy(
                                minHeight = 0,
                                minWidth = layoutWidth,
                                maxWidth = layoutWidth,
                            ),
                        )
                    placeable.placeRelative(0, layoutHeight - placeable.height)
                }

                // The indicator container is measured to fill the entire space occupied by the tab
                // row, and then placed on top of the divider.
                subcompose(TabSlots.Indicator) {
                    indicator(tabPositions)
                }.forEach {
                    it.measure(Constraints.fixed(layoutWidth, layoutHeight)).placeRelative(0, 0)
                }
            }
        }
    }
}

@Composable
private fun CustomTabTransition(
    activeColor: Color = ColorSet.select,
    inactiveColor: Color = ColorSet.text,
    selected: Boolean,
    content: @Composable () -> Unit,
) {
    val transition = updateTransition(selected, label = "")
    val color by transition.animateColor(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(
                    durationMillis = 150,
                    delayMillis = 100,
                    easing = LinearEasing,
                )
            } else {
                tween(
                    durationMillis = 100,
                    easing = LinearEasing,
                )
            }
        },
        label = "",
    ) {
        if (it) activeColor else inactiveColor
    }
    CompositionLocalProvider(
        LocalContentColor provides color,
        content = content,
    )
}

private fun Modifier.customTabIndicatorOffset2(currentTabPosition: TabPos): Modifier =
    composed(
        inspectorInfo =
            debugInspectorInfo {
                name = "tabIndicatorOffset"
                value = currentTabPosition
            },
    ) {
        val currentTabWidth by animateDpAsState(
            label = "",
            targetValue = currentTabPosition.width - 20.dp,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        )
        val indicatorOffset by animateDpAsState(
            label = "",
            targetValue = currentTabPosition.left + 10.dp,
            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset)
            .width(currentTabWidth)
    }

private data class TabPos(
    val left: Dp,
    val right: Dp,
    val width: Dp,
)

private enum class TabSlots {
    Tabs,
    Divider,
    Indicator,
}
