package com.donghyeon.dev.calculator

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.donghyeon.dev.calculator.common.LocalNavController
import com.donghyeon.dev.calculator.common.LocalViewModel
import com.donghyeon.dev.calculator.common.StartSceen
import com.donghyeon.dev.calculator.convert.ConvertAction
import com.donghyeon.dev.calculator.convert.ConvertScreen
import com.donghyeon.dev.calculator.convert.ConvertState
import com.donghyeon.dev.calculator.convert.ConvertViewModel
import com.donghyeon.dev.calculator.general.GeneralAction
import com.donghyeon.dev.calculator.general.GeneralScreen
import com.donghyeon.dev.calculator.general.GeneralState
import com.donghyeon.dev.calculator.general.GeneralViewModel
import com.donghyeon.dev.calculator.info.InfoScreen
import com.donghyeon.dev.calculator.percent.PercentAction
import com.donghyeon.dev.calculator.percent.PercentScreen
import com.donghyeon.dev.calculator.percent.PercentState
import com.donghyeon.dev.calculator.percent.PercentViewModel
import com.donghyeon.dev.calculator.ratio.RatioAction
import com.donghyeon.dev.calculator.ratio.RatioScreen
import com.donghyeon.dev.calculator.ratio.RatioState
import com.donghyeon.dev.calculator.ratio.RatioViewModel
import com.donghyeon.dev.calculator.theme.ColorSet
import com.donghyeon.dev.calculator.view.ViewTitle
import kotlinx.coroutines.flow.collectLatest

private var toast: Toast? = null

@Composable
fun MainScreen(viewModel: MainViewModel) {
    CompositionLocalProvider(
        LocalViewModel provides viewModel,
        LocalNavController provides rememberNavController(),
    ) {
        val context = LocalContext.current
        val navController = LocalNavController.current
        (LocalView.current.context as Activity).apply {
            window.statusBarColor = ColorSet.background.toArgb()
        }
        LaunchedEffect(Unit) {
            viewModel.navFlow.collectLatest {
                when (it.first) {
                    Nav.EXIT -> (context as Activity).finish()
                    Nav.POP -> navController.popBackStack()
                    Nav.POP_TO, Nav.POP_ROOT -> {
                        it.third?.let { dest ->
                            navController.popBackStack(
                                route = dest.route,
                                inclusive = false,
                            )
                        }
                    }
                    Nav.PUSH -> {
                        it.third?.let { dest ->
                            navController.navigate(dest.route)
                        }
                    }
                    Nav.POP_PUSH -> {
                        it.third?.let { dest ->
                            navController.navigate(dest.route) {
                                popUpTo(it.second.route) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            viewModel.toast.collectLatest {
                toast?.cancel()
                toast = Toast.makeText(context, it, Toast.LENGTH_SHORT)
                toast?.show()
            }
        }
        Scaffold(containerColor = ColorSet.background) {
            Column {
                NavHost(
                    modifier = Modifier.padding(it).weight(1f),
                    navController = navController,
                    startDestination = StartSceen.route,
                    enterTransition = { fadeIn(animationSpec = tween(0)) },
                    exitTransition = { fadeOut(animationSpec = tween(0)) },
                ) {
                    val pushEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            tween(100),
                        )
                    }
                    val pushPopExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            tween(100),
                        )
                    }
                    composable(Dest.MAIN.route) { MainScreen() }
                    composable(
                        route = Dest.INFO.route,
                        enterTransition = pushEnterTransition,
                        popExitTransition = pushPopExitTransition,
                    ) { InfoScreen() }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview_MainScreen_General() =
    MainScreen(
        mainState = MainState(menu = Menu.GENERAL),
        generalState = GeneralState(),
    )

@Preview
@Composable
private fun Preview_MainScreen_Percent() =
    MainScreen(
        mainState = MainState(menu = Menu.PERCENT),
        percentState = PercentState(),
    )

@Preview
@Composable
private fun Preview_MainScreen_Ratio() =
    MainScreen(
        mainState = MainState(menu = Menu.RATIO),
        ratioState = RatioState(),
    )

@Preview
@Composable
private fun Preview_MainScreen_Convert() =
    MainScreen(
        mainState = MainState(menu = Menu.CONVERT),
        convertState = ConvertState(),
    )

@Composable
private fun MainScreen() {
    val mainViewModel = LocalViewModel.current
    val generalViewModel: GeneralViewModel = hiltViewModel()
    val percentViewModel: PercentViewModel = hiltViewModel()
    val ratioViewModel: RatioViewModel = hiltViewModel()
    val convertViewModel: ConvertViewModel = hiltViewModel()
    val mainState by mainViewModel.state.collectAsState()
    val generalState by generalViewModel.state.collectAsState()
    val percentState by percentViewModel.state.collectAsState()
    val ratioState by ratioViewModel.state.collectAsState()
    val convertState by convertViewModel.state.collectAsState()
    MainScreen(
        mainState = mainState,
        mainAction = mainViewModel,
        generalState = generalState,
        generalAction = generalViewModel,
        percentState = percentState,
        percentAction = percentViewModel,
        ratioState = ratioState,
        ratioAction = ratioViewModel,
        convertState = convertState,
        convertAction = convertViewModel,
    )
    BackHandler { mainViewModel.navigation(Nav.POP, null) }
}

@Composable
private fun MainScreen(
    mainState: MainState,
    mainAction: MainAction? = null,
    generalState: GeneralState? = null,
    generalAction: GeneralAction? = null,
    percentState: PercentState? = null,
    percentAction: PercentAction? = null,
    ratioState: RatioState? = null,
    ratioAction: RatioAction? = null,
    convertState: ConvertState? = null,
    convertAction: ConvertAction? = null,
) {
    Column(
        modifier =
            Modifier
                .background(ColorSet.background)
                .fillMaxSize(),
    ) {
        mainState.menu?.let { menu ->
            ViewTitle(
                title = stringResource(id = menu.title),
                navDest = { mainAction?.navigation(Nav.PUSH, Dest.INFO) },
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            ) {
                when (menu) {
                    Menu.GENERAL ->
                        generalState?.let {
                            GeneralScreen(
                                state = it,
                                action = generalAction,
                                mainAction = mainAction,
                            )
                        }
                    Menu.PERCENT ->
                        percentState?.let {
                            PercentScreen(
                                state = it,
                                action = percentAction,
                                mainAction = mainAction,
                            )
                        }
                    Menu.RATIO ->
                        ratioState?.let {
                            RatioScreen(
                                state = it,
                                action = ratioAction,
                                mainAction = mainAction,
                            )
                        }
                    Menu.CONVERT ->
                        convertState?.let {
                            ConvertScreen(
                                state = it,
                                action = convertAction,
                                mainAction = mainAction,
                            )
                        }
                    Menu.DATE -> {}
                }
            }
            BottomMenu(
                currentMenu = menu,
                menu = { mainAction?.menu(it) },
            )
        }
    }
}

@Composable
private fun BottomMenu(
    currentMenu: Menu,
    menu: (Menu) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 1.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Menu.entries.zip(
            listOf(
                26.dp,
                24.dp,
                24.dp,
                26.dp,
                24.dp,
            ),
        ).forEach {
            Button(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(40.dp),
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = ColorSet.button,
                    ),
                elevation = null,
                onClick = { menu(it.first) },
            ) {
                Icon(
                    modifier =
                        Modifier
                            .padding(top = 1.dp)
                            .size(it.second),
                    painter = painterResource(id = it.first.icon),
                    tint =
                        if (it.first == currentMenu) {
                            ColorSet.select
                        } else {
                            ColorSet.text
                        },
                    contentDescription = "MenuIcon",
                )
            }
        }
    }
}
